package SchedulingApi;

import SchedulingApi.Exceptions.InvalidTokenException;
import SchedulingApi.Exceptions.StopAlreadyCalledException;
import SchedulingApi.Schemas.AppointmentInfo;
import SchedulingApi.Schemas.AppointmentInfoRequest;
import SchedulingApi.Schemas.AppointmentRequest;

import java.io.IOException;
import java.util.List;

public interface ISchedulingApi {

    void start() throws InvalidTokenException, IOException;

    List<AppointmentInfo> stop() throws InvalidTokenException, IOException;

    AppointmentRequest appointmentRequest() throws StopAlreadyCalledException, InvalidTokenException, IOException;

    List<AppointmentInfo> getSchedule() throws StopAlreadyCalledException, InvalidTokenException, IOException;

    boolean scheduleAppointment(AppointmentInfoRequest infoRequest) throws StopAlreadyCalledException, InvalidTokenException, IOException;

}
