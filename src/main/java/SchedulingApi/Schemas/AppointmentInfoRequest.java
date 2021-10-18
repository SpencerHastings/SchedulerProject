package SchedulingApi.Schemas;

public class AppointmentInfoRequest {
    private Integer doctorId;
    private Integer personId;
    private String appointmentTime;
    private Boolean isNewPatientAppointment;
    private Integer requestId;

    public AppointmentInfoRequest(Integer doctorId, Integer personId, String appointmentTime, Boolean isNewPatientAppointment, Integer requestId) {
        this.doctorId = doctorId;
        this.personId = personId;
        this.appointmentTime = appointmentTime;
        this.isNewPatientAppointment = isNewPatientAppointment;
        this.requestId = requestId;
    }
}
