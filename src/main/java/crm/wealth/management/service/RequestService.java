package crm.wealth.management.service;

import com.google.gson.Gson;
import crm.wealth.management.api.form.ClientInfo;
import crm.wealth.management.api.form.Person;
import crm.wealth.management.api.form.RequestForm;
import crm.wealth.management.config.ResourceNotFoundException;
import crm.wealth.management.dto.PageResponse;
import crm.wealth.management.dto.RequestDTO;
import crm.wealth.management.model.Client;
import crm.wealth.management.model.Request;
import crm.wealth.management.repository.ClientRepo;
import crm.wealth.management.repository.RequestRepo;
import crm.wealth.management.util.DataType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.SortDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RequestService {

    private final String CLIENT = "client_";
    @Autowired
    private RequestRepo requestRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private SchemaService schemaService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Request addRequest(RequestForm f) throws Exception {
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

    public PageResponse getRequestLists(Optional<String> keyword, String[] status, Optional<String> priority, Integer pageNo, Integer pageSize) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = builder.createQuery(Request.class);
        Root<Request> request = query.from(Request.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Request> requests = new ArrayList<>();

        if (!keyword.isPresent() && status == null && !priority.isPresent()) {
            requests = (List<Request>) requestRepo.findAll();
        }

        if(keyword.isPresent()) {
            predicates.add(builder.like(request.get("name"), "%" + keyword.get() + "%"));
        }

        if(status != null && status.length > 0) {
            List<DataType.REQUEST_STATUS> request_statuses = Arrays.stream(status).map(item -> DataType.REQUEST_STATUS.valueOf(item.toUpperCase())).collect(Collectors.toList());
            predicates.add(builder.in(request.get("status")).value(request_statuses));
        }

        if (priority.isPresent()) {
            predicates.add(builder.equal(request.get("priority"), DataType.REQUEST_PRIORITY.valueOf(priority.get().toUpperCase())));
        }
        query.where(predicates.toArray(new Predicate[0])).orderBy(builder.desc(request.get("createdDate")));
        requests = entityManager.createQuery(query).getResultList();

        if(requests.isEmpty()) {
            return new PageResponse();
        }
        List<RequestDTO> dtos = requests.stream().map(item -> mapper.map(item, RequestDTO.class)).collect(Collectors.toList());

        PagedListHolder<RequestDTO> pageRequest = new PagedListHolder<>(dtos);
        pageRequest.setPage(pageNo - 1);
        pageRequest.setPageSize(pageSize);

        return new PageResponse(pageRequest.getSource(), pageRequest.getNrOfElements(), pageRequest.getPageCount());
    }
}
