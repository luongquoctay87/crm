package crm.wealth.management.api.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class Property implements Serializable {
    private String value;
    private Boolean approved;
    private String comment;
}
