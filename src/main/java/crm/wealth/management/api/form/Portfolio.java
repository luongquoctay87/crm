package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class Portfolio implements Serializable {
    private Property portfolioNumber;
    private Property portfolioDescription;
    private Property portfolioType;
    private Property portfolioRiskLevel;
    private Property custodianBank;
    private Property custodianBankContact;
    private Property portfolioCurrency;
    private Property portfolioOpenDate;
    private Property portfolioStatus;
}
