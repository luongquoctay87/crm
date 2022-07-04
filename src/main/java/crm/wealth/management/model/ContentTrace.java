package crm.wealth.management.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentTrace {
    public LocalDateTime timestamp;
    public long timeTaken;
    public int status;
    public String method;
    public String uri;
    public String host;
    public String authorization;
    public String userAgent;
    public String referer;
    public String username;
    public String remoteAddress;
    public String reqBody;
    public String resBody;
}
