package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class Company implements Serializable {
    private Property companyName;
    private Property companyRegistrationNumber;
    private Property companyType;
    private Property companyJurisdiction;
    private Property companyContact;
    private Property companyAddress;
    private Property leiNumber;
}
