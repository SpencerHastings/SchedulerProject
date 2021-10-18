package SchedulingApi.Api;

import SchedulingApi.Schemas.AppointmentInfo;
import SchedulingApi.Schemas.AppointmentInfoRequest;
import SchedulingApi.Schemas.AppointmentRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface SchedulingApiService {

    @POST("/api/Scheduling/Start")
    Call<Void> postStart(@Query("token") String token);

    @POST("/api/Scheduling/Stop")
    Call<List<AppointmentInfo>> postStop(@Query("token") String token);

    @GET("/api/Scheduling/AppointmentRequest")
    Call<AppointmentRequest> getRequest(@Query("token") String token);

    @GET("/api/Scheduling/Schedule")
    Call<List<AppointmentInfo>> getSchedule(@Query("token") String token);

    @POST("/api/Scheduling/Schedule")
    Call<Void> postSchedule(@Query("token") String token, @Body AppointmentInfoRequest infoRequest);

}
