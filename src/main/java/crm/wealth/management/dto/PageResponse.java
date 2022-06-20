package crm.wealth.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResponse {

    private List<Object> requests;
    private Object requestLists;

    private int totalPages;
    private long totalElements;

    public PageResponse() {
    }

    public PageResponse(List<Object> data, long totalElements, int totalPages) {
        this.requests = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public PageResponse(RequestResponse data,  long totalElements, int totalPages) {
        this.requestLists =  data;
        this.totalPages = totalPages;
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