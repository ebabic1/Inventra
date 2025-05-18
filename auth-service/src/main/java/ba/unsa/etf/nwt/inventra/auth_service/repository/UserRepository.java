package ba.unsa.etf.nwt.inventra.auth_service.repository;

import java.util.Optional;
import java.util.UUID;

import ba.unsa.etf.nwt.inventra.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
