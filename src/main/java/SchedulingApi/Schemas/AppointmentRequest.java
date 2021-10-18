package SchedulingApi.Schemas;

import java.util.List;

public class AppointmentRequest {
    private Integer requestId;
    private Integer personId;
    private List<String> preferredDays;
    private List<Integer> preferredDocs;
    private Boolean isNew;

    public Integer getRequestId() {
        return requestId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public List<String> getPreferredDays() {
        return preferredDays;
    }

    public List<Integer> getPreferredDocs() {
        return preferredDocs;
    }

    public Boolean getNew() {
        return isNew;
    }
}
