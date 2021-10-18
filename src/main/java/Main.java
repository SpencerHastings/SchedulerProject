import Scheduler.Scheduler;
import SchedulingApi.Exceptions.InvalidTokenException;
import SchedulingApi.Exceptions.StopAlreadyCalledException;
import SchedulingApi.SchedulingApi;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String token = System.getenv("TOKEN");
        SchedulingApi api = new SchedulingApi("http://scheduling-interview-2021-265534043.us-west-2.elb.amazonaws.com",token);

        try {
            Scheduler scheduler = new Scheduler(api);
            scheduler.retrieveCurrentSchedule();
            while (scheduler.processNextAppointmentRequest()) {}
            api.stop();
            System.out.println("done");
        } catch (InvalidTokenException | IOException | StopAlreadyCalledException e) {
            e.printStackTrace();
        }

    }
}
