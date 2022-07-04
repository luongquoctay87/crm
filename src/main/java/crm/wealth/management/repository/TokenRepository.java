package crm.wealth.management.repository;

import crm.wealth.management.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

    Optional<Token> findByUsernameAndCreated(String username, long created);
}
