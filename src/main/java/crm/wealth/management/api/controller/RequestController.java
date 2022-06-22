package crm.wealth.management.api.controller;


import crm.wealth.management.api.form.RequestForm;
import crm.wealth.management.api.response.ApiResponse;
import crm.wealth.management.api.response.ErrorResponse;
import crm.wealth.management.dto.PageResponse;
import crm.wealth.management.model.Request;
import crm.wealth.management.service.RequestService;
import crm.wealth.management.util.DataType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/requests")
@Slf4j
public class RequestController {

    @Autowired
    private RequestService requestService;

    private final String APPROVED = "APPROVED";

    @GetMapping
    public ApiResponse getRequestLists(@RequestParam(value="keyword", required = false) String keyword,
                                       @RequestParam(value="status", required = false) String[] status,
                                       @RequestParam(value="priority", required = false) String priority,
                                       @RequestParam(name = "pageNo", defaultValue = "1", required = false) Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "20", required = false) Integer pageSize){
        log.info("Request api GET api/v1/requests");

        PageResponse requests = requestService.getRequestLists(Optional.ofNullable(keyword), Optional.ofNullable(status), Optional.ofNullable(priority), pageNo, pageSize);
        return new ApiResponse(HttpStatus.OK.value(), null, requests);
    }

    @PostMapping
    public ApiResponse createRequest (@RequestBody RequestForm form) {
        log.info("Request api POST api/v1/requests");

        try {
            Request request = requestService.addRequest(form);
            return new ApiResponse(HttpStatus.OK.value(), "Create request successful", request.getId());
        } catch (Exception e){
            log.error("Can not create request review");
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Create request fail");
        }
    }

    @PutMapping
    public ApiResponse updateRequest(@RequestBody RequestForm form) {
        log.info("Request api PUT api/v1/requests/{}", form.getId());

        Request request = requestService.getById(form.getId());
        try {
            requestService.saveRequest(request, form);
            return new ApiResponse(HttpStatus.OK.value(), "Update request successful", form.getId());
        } catch (Exception e){
            log.error("Can not request requestId ={}", form.getId());
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Update request fail");
        }
    }

    @PatchMapping("/{id}/assign-request")
    public ApiResponse assignRequest(@PathVariable("id") Long _id, @RequestParam Integer assignee) {
        log.info("Request api PATCH api/v1/request/{}/assign-request", _id);

        Request request = requestService.getById(_id);

        try {
            request.setAssignee(assignee);
            requestService.saveRequest(request);
            return new ApiResponse(HttpStatus.OK.value(), "Assign request successful", _id);
        } catch (Exception e){
            log.error("Can not assign request, requestId ={}", _id);
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Assign request fail");
        }
    }

    @PatchMapping("/{id}/change-priority")
    public ApiResponse changePriority(@PathVariable("id") Long _id, @RequestParam String priority) {
        log.info("Request api PATCH api/v1/request/{}/change-priority", _id);

        Request request = requestService.getById(_id);

        try {
            request.setPriority(DataType.REQUEST_PRIORITY.valueOf(priority.toUpperCase()));
            requestService.saveRequest(request);
            return new ApiResponse(HttpStatus.OK.value(), "Change priority successful", _id);
        } catch (Exception e){
            log.error("Can not change priority, requestId ={}", _id);
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Change priority fail");
        }
    }

    @PatchMapping("/{id}/change-status")
    public ApiResponse changeStatus(@PathVariable("id") Long _id, @RequestParam String status) {
        log.info("Request api PATCH api/v1/request/{}/change-status", _id);

        Request request = requestService.getById(_id);

        try {
            if (APPROVED.contentEquals(status.toUpperCase()) && !APPROVED.contentEquals(request.getStatus().toString())) {
                boolean checked = requestService.approved(request, status);
                if (!checked) {
                    return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Approve request fail");
                }
                return new ApiResponse(HttpStatus.OK.value(), "Request is approved", _id);
            }
            request.setStatus(DataType.REQUEST_STATUS.valueOf(status.toUpperCase()));
            requestService.saveRequest(request);
            return new ApiResponse(HttpStatus.OK.value(), String.format("Request is %s", status), _id);
        } catch (Exception e){
            log.error("Can not change status, requestId ={}", _id);
            return new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Change status fail");
        }
    }

}
