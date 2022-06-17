package crm.wealth.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResponse {

    private List<RequestDTO> requests;
    private Object requestLists;
    private long totalElements;

    public PageResponse() {
    }

    public PageResponse(List<RequestDTO> data, long totalElements) {
        this.requests = data;
        this.totalElements = totalElements;
    }

    public PageResponse(RequestResponse data,  long totalElements) {
        this.requestLists =  data;
        this.totalElements = totalElements;

    }

    @Data
    @NoArgsConstructor
    public static class RequestResponse {
        List<Object> newStatus = new ArrayList<>();
        List<Object> pendingStatus = new ArrayList<>();
        List<Object> reviewedStatus = new ArrayList<>() ;
        List<Object> approvedStatus = new ArrayList<>();
        List<Object> rejectedStatus = new ArrayList<>();
        List<Object> canceledStatus = new ArrayList<>();
    }
}