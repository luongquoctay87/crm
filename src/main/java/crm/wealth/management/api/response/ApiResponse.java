package crm.wealth.management.api.response;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse extends ResponseEntity<ApiResponse.Payload> {

    /**
     * Create a new {@code ApiResponse} with the given code, message, http status.
     * @param code status code
     * @param message status code message
     */
    public ApiResponse(int code, String message) {
        super(new Payload(code, message, null), HttpStatus.OK);
    }

    /**
     * Create a new {@code ApiResponse} with the given code, message, data, http status.
     * @param code status code
     * @param message status code message
     * @param data data response
     */
    public ApiResponse(int code, String message, Object data) {
        super(new Payload(code, message, data), HttpStatus.OK);
    }

    @Value
    @AllArgsConstructor
    public static class Payload {
        private int code;
        private String message;
        private Object data;
    }
}