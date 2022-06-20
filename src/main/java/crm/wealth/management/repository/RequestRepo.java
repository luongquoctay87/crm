package crm.wealth.management.repository;

import crm.wealth.management.model.Request;
import crm.wealth.management.util.DataType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RequestRepo extends PagingAndSortingRepository<Request, Long>{
    Page<Request> findByNameLike(String name, Pageable pageable);

    Page<Request> findByNameLikeAndStatusIn(String s, List<DataType.REQUEST_STATUS> request_statuses, Pageable pageable);

    Page<Request> findByNameLikeAndPriority(String s, DataType.REQUEST_PRIORITY request_priority, Pageable pageable);

    Page<Request> findByNameLikeAndStatusInAndPriority(String s, List<DataType.REQUEST_STATUS> request_statuses, DataType.REQUEST_PRIORITY request_priority, Pageable pageable);

    Page<Request> findByStatusInAndPriority(List<DataType.REQUEST_STATUS> request_statuses, DataType.REQUEST_PRIORITY request_priority, Pageable pageable);

    Page<Request> findByStatusIn(List<DataType.REQUEST_STATUS> request_statuses, Pageable pageable);

    Page<Request> findByPriority(DataType.REQUEST_PRIORITY request_priority, Pageable pageable);
}
