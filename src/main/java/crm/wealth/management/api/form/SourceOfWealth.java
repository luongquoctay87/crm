package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class SourceOfWealth implements Serializable {
    private Property personalBackground;
    private Property professionalBusinessBackground;
    private Property mainSourceIncome;
    private Property otherMainSourceIncome;
    private Property growthAndPlan;
    private Property economicPurposeAndRationale;
    private Property estimatedWealth;
    private Property estimatedAnnualIncome;
    private Property sourceFunds;
    private Property sourceOfWealthCorroboration;
    private Property sowPartyDetails;
}
