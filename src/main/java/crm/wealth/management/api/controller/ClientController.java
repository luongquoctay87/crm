package crm.wealth.management.api.controller;

import crm.wealth.management.api.form.ClientForm;
import crm.wealth.management.api.form.CompanyForm;
import crm.wealth.management.model.Client;
import crm.wealth.management.service.ClientService;
import crm.wealth.management.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@Slf4j
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private SchemaService schemaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client createClient(@RequestBody ClientForm clientForm) {
        return clientService.addClient(clientForm);
    }

    @GetMapping
    public void createSchema() {
        schemaService.createSchema("client_1");
        schemaService.createTable("client_1");
        //'IBM','a123-ccc-abc','company_type_2','jurisdiction','contact','California - USA',334456
        CompanyForm form = CompanyForm.builder()
                .companyName("IBM")
                .companyRegistrationNumber("a123-ccc-abc")
                .companyType("company_type_1")
                .companyJurisdiction("jurisdiction")
                .companyContact("contact")
                .companyAddress("California - USA")
                .leiNumber(334456)
                .build();
        schemaService.createData("client_1", form);
    }
}
