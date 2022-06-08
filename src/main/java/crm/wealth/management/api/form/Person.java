package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {
    private Property title;
    private Property firstName;
    private Property middleName;
    private Property surname;
    private Property fullName;
    private Property personType;
    private Property alias;
    private Property formerName;
    private Property nativeName;
    private Property dateOfBirth;
    private Property gender;
    private Property nationality;
    private Property nationality2;
    private Property birthCity;
    private Property birthCountry;
    private Property residenceCountry;
    private Property taxResidency;
    private Property taxResidency2;
    private Property taxResidency3;
    private Property personIdDocument;
    private Property personIdNumber;
    private Property clientRiskProfile;
    private Property pepStatus;
    private Property status;
    private Property occupation;
}
