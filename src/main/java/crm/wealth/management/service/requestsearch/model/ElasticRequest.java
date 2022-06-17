package crm.wealth.management.service.requestsearch.model;

import crm.wealth.management.util.DataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.VersionType;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Document(
        indexName = "${app.elasticsearch.user.index}",
        createIndex = true
)
@Builder
@Data
public class ElasticRequest {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String comment;

    @Field(type = FieldType.Text)
    private String type;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Text)
    private String priority;

    @Field(type = FieldType.Integer)
    private Integer assignee;

    @Field(type = FieldType.Integer)
    private Integer createdBy;

    @Field(type = FieldType.Integer)
    private Integer checkedBy;

    @Field(type = FieldType.Integer)
    private Integer approvedBy;

    @Field(type = FieldType.Integer)
    private Integer lastModifiedBy;

    @Field(type = FieldType.Date)
    private Date createdDate;

    @Field(type = FieldType.Date)
    private Date checkedDate;

    @Field(type = FieldType.Date)
    private Date approvedDate;

    @Field(type = FieldType.Date)
    private Date lastModifiedDate;
}
