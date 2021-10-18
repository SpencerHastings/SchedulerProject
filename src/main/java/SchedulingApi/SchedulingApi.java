package SchedulingApi;

import SchedulingApi.Api.SchedulingApiService;
import SchedulingApi.Exceptions.InvalidTokenException;
import SchedulingApi.Exceptions.StopAlreadyCalledException;
import SchedulingApi.Schemas.AppointmentInfo;
import SchedulingApi.Schemas.AppointmentInfoRequest;
import SchedulingApi.Schemas.AppointmentRequest;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class SchedulingApi implements ISchedulingApi{

    public SchedulingApi(String baseURL, String token) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        rf = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        api = rf.create(SchedulingApiService.class);
        this.token = token;
    }

    private final Retrofit rf;
    private final SchedulingApiService api;
    private final String token;

    @Override
    public void start() throws InvalidTokenException, IOException {
        Call<Void> call = api.postStart(token);

        Response<Void> response = call.execute();
        if (response.code() == 401) {
            throw new InvalidTokenException();
        } else if (response.code() != 200) {
            throw new IOException();
        }
    }

    @Override
    public List<AppointmentInfo> stop() throws InvalidTokenException, IOException {
        Call<List<AppointmentInfo>> call = api.postStop(token);

        Response<List<AppointmentInfo>> response = call.execute();
        if (response.code() == 200) {
            return response.body();
        } else if (response.code() == 401) {
            throw new InvalidTokenException();
        } else {
            throw new IOException();
        }
    }

    @Override
    public AppointmentRequest appointmentRequest() throws StopAlreadyCalledException, InvalidTokenException, IOException {
        Call<AppointmentRequest> call = api.getRequest(token);

        Response<AppointmentRequest> response = call.execute();
        if (response.code() == 200) {
            return response.body();
        } else if (response.code() == 204) {
            return null;
        } else if (response.code() == 401) {
            throw new InvalidTokenException();
        } else if (response.code() == 405) {
            throw new StopAlreadyCalledException();
        } else {
            throw new IOException();
        }
    }

    @Override
    public List<AppointmentInfo> getSchedule() throws StopAlreadyCalledException, InvalidTokenException, IOException {
        Call<List<AppointmentInfo>> call = api.getSchedule(token);

        Response<List<AppointmentInfo>> response = call.execute();
        if (response.code() == 200) {
            return response.body();
        } else if (response.code() == 401) {
            throw new InvalidTokenException();
        } else if (response.code() == 405) {
            throw new StopAlreadyCalledException();
        } else {
            throw new IOException();
        }
    }

    @Override
    public boolean scheduleAppointment(AppointmentInfoRequest infoRequest) throws StopAlreadyCalledException, InvalidTokenException, IOException {
        Call<Void> call = api.postSchedule(token, infoRequest);

        Response<Void> response = call.execute();
        if (response.code() == 200) {
            return true;
        } else if (response.code() == 401) {
            throw new InvalidTokenException();
        } else if (response.code() == 405) {
            throw new StopAlreadyCalledException();
        } else if (response.code() == 500) {
            return false;
        } else {
            throw new IOException();
        }
    }
}
