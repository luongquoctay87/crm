package crm.wealth.management.api.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CompanyForm {
    private String companyName;
    private String companyRegistrationNumber;
    private String companyType;
    private String companyJurisdiction;
    private String companyContact;
    private String companyAddress;
    private int leiNumber;
    private Date createdDate;
    private Date updatedDate;
}
