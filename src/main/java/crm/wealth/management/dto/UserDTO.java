package crm.wealth.management.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String middleName;
    private String surname;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String username;
    private Date createdDate;
    private Date updatedDate;

}
