package crm.wealth.management.api.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserForm implements Serializable {
    private Long id;
    private String firstName;
    private String middleName;
    private String surname;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String username;
    private String password;
    private String rePassword;
    private Date createdDate;
    private Date updatedDate;
}
