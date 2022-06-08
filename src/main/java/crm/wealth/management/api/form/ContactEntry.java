package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class ContactEntry implements Serializable {
    private Property contactDateAndTime;
    private Property contactType;
    private Property location;
    private Property subjectDiscussion;
    private Property contentOfDiscussion;
    private Property actionRequired;
    private Property dueDate;
}
