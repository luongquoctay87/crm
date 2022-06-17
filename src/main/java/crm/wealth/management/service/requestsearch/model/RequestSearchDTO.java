package crm.wealth.management.service.requestsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;

@Data
public class RequestSearchDTO {

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
