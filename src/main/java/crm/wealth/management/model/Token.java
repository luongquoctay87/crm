package crm.wealth.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tbl_tokens")
@AllArgsConstructor
@NoArgsConstructor
public class Token implements Serializable {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "token")
    private String token;

    @Column(name = "created")
    private long created;

    @JsonIgnore
    @Column(name = "ip_clients")
    private String ipClients;
}
