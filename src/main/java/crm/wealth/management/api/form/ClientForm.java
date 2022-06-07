package crm.wealth.management.api.form;

import lombok.Data;

@Data
public class ClientForm {
    private String firstName;
    private String middleName;
    private String surname;
    private String fullName;
    private String phone;
    private String email;
    private String address;
}
