package crm.wealth.management.dto;

import crm.wealth.management.util.DataType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RequestDTO implements Serializable {

    private Long id;

    private String name;

    private String comment;

    private DataType.REQUEST_TYPE type;

    private DataType.REQUEST_STATUS status;

    private DataType.REQUEST_PRIORITY priority;

    private Integer assignee;

    private Integer createdBy;

    private Integer checkedBy;

    private Integer approvedBy;

    private Integer lastModifiedBy;

    private Date createdDate;

    private Date checkedDate;

    private Date approvedDate;

    private Date lastModifiedDate;
}
