package crm.wealth.management.model;


import crm.wealth.management.util.DataType;
import crm.wealth.management.util.PostgresEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(
        name = "e_client",
        typeClass = PostgresEnumType.class
)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "person_type")
    @Enumerated(EnumType.STRING)
    @Type(type = "e_client")
    private DataType.PERSON_TYPE personType;

    @Column(name = "alias")
    private String alias;

    @Column(name = "former_name")
    private String formerName;

    @Column(name = "native_name")
    private String nativeName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    @Type(type = "e_client")
    private DataType.GENDER gender;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "nationality_2")
    private String nationality2;

    @Column(name = "birth_city")
    private String birthCity;

    @Column(name = "birth_country")
    private String birthCountry;

    @Column(name = "residence_country")
    private String residenceCountry;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;
}
