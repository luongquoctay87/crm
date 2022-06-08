package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class Document implements Serializable {
    private Property documentType;
    private Property documentNumber;
    private Property documentIssueDate;
    private Property documentExpiry;
    private Property documentCountryOfIssuance;
    private Property comments;
    private Property mandatoryFlag;
    private Property expectedDateOfReceipt;
    private Property actualDateOfReceipt;
    private Property status;
}
