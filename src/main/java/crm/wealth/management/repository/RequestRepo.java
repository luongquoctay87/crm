package crm.wealth.management.repository;

import crm.wealth.management.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {

}
