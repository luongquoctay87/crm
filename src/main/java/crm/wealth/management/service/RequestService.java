package crm.wealth.management.service;

import com.google.gson.Gson;
import crm.wealth.management.api.form.ClientInfo;
import crm.wealth.management.api.form.Person;
import crm.wealth.management.api.form.RequestForm;
import crm.wealth.management.config.ResourceNotFoundException;
import crm.wealth.management.model.Client;
import crm.wealth.management.model.Request;
import crm.wealth.management.repository.ClientRepo;
import crm.wealth.management.repository.RequestRepo;
import crm.wealth.management.util.DataType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public long count() {
        return requestRepo.count();
    }

    public List<Request> findAll(Optional<String> search, Optional<String[]> status, Optional<String[]> priority, String[] sort, int pageNo, int pageSize) {

        String keyword = "%";
        if (search.isPresent()) {
            keyword = "%" + search.get() + "%";
        }

        List<String> statuses = new ArrayList<>();
        if (status.isPresent() && status.get().length > 0) {
            for (int i = 0; i < status.get().length; i++) {
                String st = status.get()[i];
                statuses.add(st.toUpperCase());
            }
        } else {
            statuses = getStatusList();
        }

        List<String> priorities = new ArrayList<>();
        if (priority.isPresent() && priority.get().length > 0) {
            for (int i = 0; i < priority.get().length; i++) {
                String p = priority.get()[i];
                priorities.add(p.toUpperCase());
            }
        } else {
            priorities = getPriorityList();
        }

        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            // will sort more than 2 columns
            for (String sortOrder : sort) {
                // sortOrder="column, direction"
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[column, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        if (pageNo > 0) pageNo = pageNo - 1;

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(orders));
        Page<Request> pagedResult = requestRepo.findAllRequest(keyword, statuses, priorities, pageable);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Request>();
        }
    }

    private List<String> getPriorityList() {
        List<String> priority = new ArrayList();
        priority.add(DataType.REQUEST_PRIORITY.LOW.toString());
        priority.add(DataType.REQUEST_PRIORITY.MEDIUM.toString());
        priority.add(DataType.REQUEST_PRIORITY.HIGH.toString());
        return priority;
    }

    private List<String> getStatusList() {
        List<String> statuses = new ArrayList();
        statuses.add(DataType.REQUEST_STATUS.NEW.toString());
        statuses.add(DataType.REQUEST_STATUS.PENDING.toString());
        statuses.add(DataType.REQUEST_STATUS.REVIEWED.toString());
        statuses.add(DataType.REQUEST_STATUS.APPROVED.toString());
        statuses.add(DataType.REQUEST_STATUS.REJECTED.toString());
        statuses.add(DataType.REQUEST_STATUS.CANCEL.toString());
        return statuses;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }
}
