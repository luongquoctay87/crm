package crm.wealth.management.api.form;

import crm.wealth.management.util.DataType;
import lombok.Data;

import java.io.Serializable;

@Data
public class RequestForm implements Serializable {
    private Long id;
    private String name;
    private ClientInfo clientInfo;
    private String comment;
    private DataType.REQUEST_TYPE type;
    private DataType.REQUEST_STATUS status;
    private DataType.REQUEST_PRIORITY priority;
    private Integer assignee;
    private Integer createdBy;
    private Integer checkedBy;
    private Integer approvedBy;
    private Integer lastModifyBy;

}
