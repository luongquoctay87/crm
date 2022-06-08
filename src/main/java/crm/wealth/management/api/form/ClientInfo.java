package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientInfo implements Serializable {
    private Person person;
    private Company company;
    private Portfolio portfolio;
    private SourceOfWealth sourceOfWealth;
    private Contact contact;
    private ContactEntry contactEntry;
    private Document document;
}
