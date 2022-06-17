package crm.wealth.management.repository;

import crm.wealth.management.model.Request;
import crm.wealth.management.util.DataType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RequestRepo extends PagingAndSortingRepository<Request, Long> {
    Page<Request> findAll(Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.name LIKE :keyword")
    Page<Request> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.name LIKE :keyword AND r.status in (:request_statuses)")
    Page<Request> findByKeywordAndStatus(String keyword, List<DataType.REQUEST_STATUS> request_statuses, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.name LIKE :keyword AND r.priority = :priority ")
    Page<Request> findByKeywordAndPriority(String keyword, DataType.REQUEST_PRIORITY priority, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.name LIKE :keyword AND r.priority = :priority AND r.status in (:request_statuses)")
    Page<Request> findByKeywordAndStatusAndPriority(String keyword, List<DataType.REQUEST_STATUS> request_statuses, DataType.REQUEST_PRIORITY priority, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE  r.priority = :priority AND r.status in (:request_statuses)")
    Page<Request> findByStatusAndPriority(List<DataType.REQUEST_STATUS> request_statuses, DataType.REQUEST_PRIORITY priority, Pageable pageable);

    @Query("SELECT r FROM Request r WHERE r.status in (:request_statuses)")
    Page<Request> findByStatus(List<DataType.REQUEST_STATUS> request_statuses, Pageable pageable);
    @Query("SELECT r FROM Request r WHERE  r.priority = :priority")
    Page<Request> findByPriority(DataType.REQUEST_PRIORITY priority, Pageable pageable);
}
