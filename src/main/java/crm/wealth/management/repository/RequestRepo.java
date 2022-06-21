package crm.wealth.management.repository;

import crm.wealth.management.model.Request;
import crm.wealth.management.util.DataType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepo extends PagingAndSortingRepository<Request, Long>{

}
