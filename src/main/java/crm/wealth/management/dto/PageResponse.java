package crm.wealth.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResponse {

    private List<RequestDTO> requests;
    private int totalPages;
    private long count;

    public PageResponse() {
    }

    public PageResponse(List<RequestDTO> data, long count, int totalPages) {
        this.requests = data;
        this.totalPages = totalPages;
        this.count = count;
    }
}