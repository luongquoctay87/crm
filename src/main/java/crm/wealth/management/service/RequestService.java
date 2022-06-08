package crm.wealth.management.service;

import com.google.gson.Gson;
import crm.wealth.management.api.form.RequestForm;
import crm.wealth.management.config.ResourceNotFoundException;
import crm.wealth.management.model.Client;
import crm.wealth.management.api.form.ClientInfo;
import crm.wealth.management.api.form.Person;
import crm.wealth.management.model.Request;
import crm.wealth.management.repository.ClientRepo;
import crm.wealth.management.repository.RequestRepo;
import crm.wealth.management.util.DataType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class RequestService {

    @Autowired
    private RequestRepo requestRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private SchemaService schemaService;

    private final String CLIENT = "client_";

    @Transactional
    public Request addRequest(RequestForm f) {
        String content = new Gson().toJson(f.getClientInfo());
        Request request = Request.builder()
                .name(f.getName())
                .clientInfo(content)
                .comment(f.getComment())
                .type(f.getType())
                .status(f.getStatus())
                .priority(f.getPriority())
                .assignee(f.getAssignee())
                .createdBy(f.getCreatedBy())
                .createdDate(new Date())
                .build();
        return requestRepo.save(request);
    }

    public Request getById(long id) {
        Optional<Request> request = requestRepo.findById(id);
        if (!request.isPresent()) {
            throw new ResourceNotFoundException("Request not found", "requestId", id);
        }
        return request.get();
    }


    public void saveRequest(Request request) {
        requestRepo.save(request);
    }

    @Transactional
    public void saveRequest(Request request, RequestForm f) {
        String content = new Gson().toJson(f.getClientInfo());
        request.setName(f.getName());
        request.setClientInfo(content);
        request.setComment(f.getComment());
        request.setAssignee(f.getAssignee());
        request.setStatus(f.getStatus());
        request.setPriority(f.getPriority());
        request.setLastModifiedBy(f.getLastModifyBy());
        request.setLastModifiedDate(new Date());

        requestRepo.save(request);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean approved(Request request, String status) throws Exception {
        // create client
        Gson gson = new Gson();
        ClientInfo clientInfo = gson.fromJson(request.getClientInfo(), ClientInfo.class);
        Person person = clientInfo.getPerson();

        // Todo: check client before execute coding below

        Client client = Client.builder()
                .firstName(person.getFirstName().getValue())
                .middleName(person.getMiddleName().getValue())
                .surname(person.getSurname().getValue())
                .fullName(person.getFullName().getValue())
                .personType(DataType.PERSON_TYPE.valueOf(person.getPersonType().getValue().toUpperCase()))
                .alias(person.getAlias().getValue())
                .formerName(person.getFormerName().getValue())
                .nativeName(person.getNativeName().getValue())
                .dateOfBirth(toDate(person.getDateOfBirth().getValue()))
                .gender(DataType.GENDER.valueOf(person.getGender().getValue().toUpperCase()))
                .nationality(person.getNationality().getValue())
                .nationality2(person.getNationality2().getValue())
                .birthCity(person.getBirthCity().getValue())
                .birthCountry(person.getBirthCountry().getValue())
                .residenceCountry(person.getResidenceCountry().getValue())
                .createdDate(new Date())
                .updatedDate(new Date())
                .build();

        Client saveClient = clientRepo.save(client);

        if (saveClient != null) {
            // create schema
            String schema = CLIENT + saveClient.getId();
            schemaService.createSchema(schema);

            // save client information
            schemaService.saveData(schema, clientInfo);

            // change status
            request.setStatus(DataType.REQUEST_STATUS.APPROVED);
            requestRepo.save(request);
            return true;
        }

        return false;
    }

    private Date toDate(String dateOfBirth) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
        try {
            return formatter.parse(dateOfBirth);
        } catch (ParseException e) {
            log.error("Convert date of birth fail");
        }
        return new Date();
    }

}
