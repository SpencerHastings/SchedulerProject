package Scheduler;

import SchedulingApi.Exceptions.InvalidTokenException;
import SchedulingApi.Exceptions.StopAlreadyCalledException;
import SchedulingApi.ISchedulingApi;
import SchedulingApi.Schemas.AppointmentInfo;
import SchedulingApi.Schemas.AppointmentInfoRequest;
import SchedulingApi.Schemas.AppointmentRequest;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Scheduler {

    private final ISchedulingApi api;

    private final Map<String, AppointmentInfo> appointments;

    public Scheduler(ISchedulingApi api) throws InvalidTokenException, IOException {
        this.api = api;
        appointments = new HashMap<>();
        api.start();
    }

    public void retrieveCurrentSchedule() throws StopAlreadyCalledException, InvalidTokenException, IOException {
        List<AppointmentInfo> apps = api.getSchedule();
        for (AppointmentInfo app : apps) {
            String key = app.getAppointmentTime() + app.getDoctorId();
            if (appointments.containsKey(key)) {
                System.out.println("Dup");
            }
            appointments.put(key, app);
        }
    }

    public boolean processNextAppointmentRequest() throws StopAlreadyCalledException, InvalidTokenException, IOException {
        AppointmentRequest request = api.appointmentRequest();
        if (request == null) {
            return false;
        }
        for (String dateString : request.getPreferredDays()) {
            if (!checkDate(dateString)) {
                continue;
            }
            if (!checkPatient(request.getPersonId(), dateString)) {
                continue;
            }
            String pickedDate = null;
            int doctorId = 0;
            for (int doctor : request.getPreferredDocs()) {
                pickedDate = chooseTime(dateString, doctor, request.getNew());
                if (pickedDate != null) {
                    doctorId = doctor;
                    break;
                }
            }

            if (doctorId == 0) {
                continue;
            }

            AppointmentInfoRequest req = createInfoRequest(request, doctorId, pickedDate);
            if (api.scheduleAppointment(req)) {
                System.out.println("success");
                AppointmentInfo appInfo = new AppointmentInfo(doctorId, request.getPersonId(), pickedDate, request.getNew());
                String key = pickedDate + doctorId;
                if (appointments.containsKey(key)) {
                    System.out.println("Dup");
                }
                appointments.put(key, appInfo);
            } else {
                System.out.println("rejected by system");
            }
            return true;
        }
        System.out.println("impossible");
        return true;
    }

    private boolean checkDate(String dateString) {
        LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY){
            return false;
        } else if (date.getMonth() != Month.NOVEMBER && date.getMonth() != Month.DECEMBER){
            return false;
        } else return date.getYear() == 2021;
    }

    private boolean checkPatient(int patientId, String date) {
        List<AppointmentInfo> patientApps = getPersonAppointments(patientId);
        LocalDateTime dateToCheck = LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        int dayOfYear = dateToCheck.getDayOfYear();
        for (AppointmentInfo app : patientApps) {
            LocalDateTime appToCheck = LocalDateTime.parse(app.getAppointmentTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            int appDayOfYear = appToCheck.getDayOfYear();
            if (Math.abs(dayOfYear - appDayOfYear) < 7) {
                return false;
            }
        }
        return true;
    }

    private String chooseTime(String dateString, int doctorId, boolean isNew) {
        LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if (!isNew) {
            for (int i = 8; i <= 14; i++) {
                LocalDateTime dateCheck = date.withHour(i);
                String key = dateCheck + ":00Z" + doctorId;
                if (!appointments.containsKey(key)) {
                    return dateCheck + ":00Z";
                }
            }
        } else {
            for (int i = 15; i <= 16; i++) {
                LocalDateTime dateCheck = date.withHour(i);
                String key = dateCheck + ":00Z" + doctorId;
                if (!appointments.containsKey(key)) {
                    return dateCheck + ":00Z";
                }
            }
        }
        return null;
    }

    private List<AppointmentInfo> getPersonAppointments(int patientId) {
        List<AppointmentInfo> patientApps = new ArrayList<>();
        for (AppointmentInfo app : appointments.values()) {
            if (app.getPersonId() == patientId) {
                patientApps.add(app);
            }
        }
        return patientApps;
    }

    private AppointmentInfoRequest createInfoRequest(AppointmentRequest request, int chosenDoctor, String chosenTime) {
        return new AppointmentInfoRequest(chosenDoctor, request.getPersonId(), chosenTime, request.getNew(), request.getRequestId());
    }
}
