package crm.wealth.management.api.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ErrorResponse extends ApiResponse {

    public ErrorResponse() {
        super(HttpStatus.BAD_REQUEST.value(), "Error");
    }

    public ErrorResponse(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public ErrorResponse(String message, Object data) {
        super(HttpStatus.BAD_REQUEST.value(), message, data);
    }

    public ErrorResponse(int code, String message) {
        super(code, message);
    }

    @Value
    @AllArgsConstructor
    public static class ApiError {
        List<Error> errors;
    }

    @Value
    @AllArgsConstructor
    public static class Error {
        private String field;
        private Object value;
        private String message;
    }
}
