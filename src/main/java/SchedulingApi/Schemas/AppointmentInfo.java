package SchedulingApi.Schemas;

public class AppointmentInfo {
    private Integer doctorId;
    private Integer personId;
    private String appointmentTime;
    private Boolean isNewPatientAppointment;

    public AppointmentInfo(Integer doctorId, Integer personId, String appointmentTime, Boolean isNewPatientAppointment) {
        this.doctorId = doctorId;
        this.personId = personId;
        this.appointmentTime = appointmentTime;
        this.isNewPatientAppointment = isNewPatientAppointment;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public Boolean getNewPatientAppointment() {
        return isNewPatientAppointment;
    }
}
