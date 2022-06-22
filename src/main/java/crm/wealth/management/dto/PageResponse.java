package crm.wealth.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResponse {

    private List<RequestDTO> requests;
    private long count;

    public PageResponse() {
    }

    public PageResponse(List<RequestDTO> data, long count) {
        this.requests = data;
        this.count = count;
    }
}

