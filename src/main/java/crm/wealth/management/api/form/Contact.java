package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class Contact implements Serializable {
    private Property registeredAddress;
    private Property correspondenceAddress;
    private Property otherAddress;
    private Property registeredContactNumber;
    private Property registeredEmailAddress;
    private Property preferredContactMethod;
}
