package crm.wealth.management.repository;

import crm.wealth.management.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepo extends JpaRepository<Client, Long> {

    Client findByEmail(String email);
}
