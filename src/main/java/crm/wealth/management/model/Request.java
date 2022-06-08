package crm.wealth.management.model;

import crm.wealth.management.util.DataType;
import crm.wealth.management.util.PostgresEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TypeDef(
    name = "e_request",
    typeClass = PostgresEnumType.class
)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "client_info")
    private String clientInfo;

    @Column(name = "comment")
    private String comment;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @Type(type = "e_request")
    private DataType.REQUEST_TYPE type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Type(type = "e_request")
    private DataType.REQUEST_STATUS status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    @Type(type = "e_request")
    private DataType.REQUEST_PRIORITY priority;

    @Column(name = "assignee")
    private Integer assignee;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "checked_by")
    private Integer checkedBy;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "last_modified_by")
    private Integer lastModifiedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "checked_date")
    private Date checkedDate;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
}
