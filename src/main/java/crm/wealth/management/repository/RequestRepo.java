package crm.wealth.management.repository;

import crm.wealth.management.model.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Long> {

    @Query(value = "FROM Request r WHERE r.name LIKE :keyword AND cast (r.status as text) IN :status AND cast (r.priority as text) IN :priority")
    Page<Request> findAllRequest(String keyword, List<String> status, List<String> priority, Pageable pageable);
}
