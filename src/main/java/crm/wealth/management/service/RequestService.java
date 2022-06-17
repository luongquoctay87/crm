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
import crm.wealth.management.rabbitmq.InitIndexMessage;
import crm.wealth.management.rabbitmq.MessageSender;
import crm.wealth.management.rabbitmq.MqMessage;
import crm.wealth.management.rabbitmq.RabbitConfig;
import crm.wealth.management.repository.ClientRepo;
import crm.wealth.management.repository.RequestRepo;
import crm.wealth.management.service.requestsearch.model.ElasticRequest;
import crm.wealth.management.service.requestsearch.service.RequestSearchService;
import crm.wealth.management.util.DataType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private RequestSearchService requestSearchService;

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private ModelMapper mapper;

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
        request = requestRepo.save(request);

//        Send message to RabbitMQ
        try {
            InitIndexMessage message = new InitIndexMessage(request);

            messageSender.send(new MqMessage(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, message));
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception();
        }

        return request;
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

    /**
     * Search and Paging list request
     * Search by request name, sort by status ( New, Pending, Approved, Rejected ), sort by priority ( Low, Medium, High ).
     *
     * @param keyword
     * @param status
     * @param priority
     * @param pageNo
     * @param pageSize
     * @return
     */
//    public PageResponse getRequestLists(Optional<String> keyword, String[] status, Optional<String> priority, Integer pageNo, Integer pageSize) throws Exception {
//
//        List<DataType.REQUEST_STATUS> request_statuses = new ArrayList<>();
//        DataType.REQUEST_PRIORITY request_priority = null;
//
//        if (status != null && status.length > 0) {
//            for (int i = 0; i < status.length; i++) {
//                DataType.REQUEST_STATUS statusRequest = DataType.REQUEST_STATUS.valueOf(status[i].toUpperCase());
//                request_statuses.add(statusRequest);
//            }
//        }
//
//        if (priority.isPresent()) {
//            request_priority = DataType.REQUEST_PRIORITY.valueOf(priority.get().toUpperCase());
//        }
//
//        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.Direction.DESC, "createdDate");
//
//        Page<Request> requests = null;
//
//        try {
//            //        Case all params null
//            if (!keyword.isPresent() && status == null && !priority.isPresent()) {
//                requests = requestRepo.findAll(pageable);
//            }
//            //        Case has keyword
//            if (keyword.isPresent() && status == null && !priority.isPresent()) {
//                requests = requestRepo.findByKeyword(keyword.get(), pageable);
//            }
//            //        Case have keyword and status
//            if (keyword.isPresent() && status != null && status.length > 0 && !priority.isPresent()) {
//                requests = requestRepo.findByKeywordAndStatus(keyword.get(), request_statuses, pageable);
//            }
//            //        Case have keyword and priority
//            if (keyword.isPresent() && priority.isPresent() && status == null) {
//                requests = requestRepo.findByKeywordAndPriority(keyword.get(), request_priority, pageable);
//            }
//            //        Case have keyword, status and priority
//            if (keyword.isPresent() && priority.isPresent() && status != null && status.length > 0) {
//                requests = requestRepo.findByKeywordAndStatusAndPriority(keyword.get(), request_statuses, request_priority, pageable);
//            }
//            //        Case have status and priority
//            if (!keyword.isPresent() && status != null && status.length > 0 && priority.isPresent()) {
//                requests = requestRepo.findByStatusAndPriority(request_statuses, request_priority, pageable);
//            }
//            //        Case have status
//            if (!keyword.isPresent() && status != null && status.length > 0 && !priority.isPresent()) {
//                requests = requestRepo.findByStatus(request_statuses, pageable);
//            }
//            //        Case have priority
//            if (!keyword.isPresent() && status == null && priority.isPresent()) {
//                requests = requestRepo.findByPriority(request_priority, pageable);
//            }
//
//            if (requests == null) {
//                log.error("Error when query list request from database");
//                throw new Exception("Error when query list request from database");
//            }
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//        List<RequestDTO> dtos = requests.getContent().stream().map(request -> mapper.map(request, RequestDTO.class)).collect(Collectors.toList());
//
//
//        if (status == null) {
//
//            try {
//                PageResponse pageResponse = new PageResponse(Collections.singletonList(dtos), requests.getTotalPages(), requests.getTotalElements(), requests.hasNext(), requests.hasPrevious());
//                return pageResponse;
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                throw new Exception(e.getMessage());
//            }
//
//        }
//
//        PageResponse.RequestResponse requestResponse = new PageResponse.RequestResponse();
//
//        for (int i = 0; i < dtos.size(); i++) {
//
//            RequestDTO requestDTO = dtos.get(i);
//
//            DataType.REQUEST_STATUS requestStatus = requestDTO.getStatus();
//
//            switch (requestStatus) {
//                case NEW:
//                    requestResponse.getNewStatus().add(requestDTO);
//                    break;
//                case PENDING:
//                    requestResponse.getPendingStatus().add(requestDTO);
//                    break;
//                case REVIEWED:
//                    requestResponse.getReviewedStatus().add(requestDTO);
//                    break;
//                case APPROVED:
//                    requestResponse.getApprovedStatus().add(requestDTO);
//                    break;
//                case REJECTED:
//                    requestResponse.getRejectedStatus().add(requestDTO);
//                    break;
//                case CANCEL:
//                    requestResponse.getCanceledStatus().add(requestDTO);
//                    break;
//                default:
//            }
//        }
//
//        PageResponse pageResponse = new PageResponse();
//        pageResponse.setRequestLists(requestResponse);
//        pageResponse.setTotalPages(requests.getTotalPages());
//        pageResponse.setTotalElements(requests.getTotalElements());
//        pageResponse.setHasNext(requests.hasNext());
//        pageResponse.setHasPrevious(requests.hasPrevious());
//
//        return pageResponse;
//
//    }

    public PageResponse getRequestLists(Optional<String> keyword, String[] status, Optional<String> priority, Integer pageNo, Integer pageSize) {

        Map<String, Object> map = requestSearchService.search(keyword, status, priority, pageNo, pageSize);

        try {
            List<ElasticRequest> elasticRequestLists = (List<ElasticRequest>) map.get("data");

            if(elasticRequestLists == null) {
                log.error("Data from elasticsearch query is null");
                throw new Exception("Data from elasticsearch query is null");
            }

            List<RequestDTO> data = (List<RequestDTO>) elasticRequestLists.stream().map(item -> mapper.map(item, RequestDTO.class));

            if(data == null) {
                log.error("Error when parse data from elasticsearch query to RequestDTO");
                throw new Exception("Error when parse data from elasticsearch query to RequestDTO");
            }

            PageResponse pageResponse = new PageResponse( data, (Long) map.get("totalElements"));

            return pageResponse;
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
