package crm.wealth.management.service;

import crm.wealth.management.api.form.ClientForm;
import crm.wealth.management.model.Client;
import crm.wealth.management.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClientService {

    @Autowired
    private ClientRepo clientRepo;

    public Client addClient(ClientForm form){
        Client client = Client.builder()
                .firstName(form.getFirstName())
                .middleName(form.getMiddleName())
                .surname(form.getSurname())
                .fullName(form.getFullName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .address(form.getAddress())
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();

        return clientRepo.save(client);
    }
}
