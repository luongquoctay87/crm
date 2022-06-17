package crm.wealth.management.dto;

import crm.wealth.management.util.DataType;
import lombok.Data;
import java.util.Date;

@Data
public class RequestDTO {

    private Long id;

    private String name;

    private String comment;

    private String type;

    private String status;

    private String priority;

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
