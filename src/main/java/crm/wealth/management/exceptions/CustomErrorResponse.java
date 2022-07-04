package crm.wealth.management.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import crm.wealth.management.api.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CustomErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private List<String> errors;
    private HttpStatus status;

    public CustomErrorResponse(final HttpStatus status, final String message, LocalDateTime timestamp,
                               final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public CustomErrorResponse(final HttpStatus status, final String message, LocalDateTime timestamp,
                               final String error) {
        super();
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        errors = Arrays.asList(error);
    }

    public CustomErrorResponse(HttpStatus status) {
        this.status = status;
    }

    public void getErrorResponse(CustomErrorResponse e) {
        new ApiResponse(e.status.value(), e.message);
    }
}
