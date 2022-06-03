package crm.wealth.management.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private String key;
    private Object value;

}
