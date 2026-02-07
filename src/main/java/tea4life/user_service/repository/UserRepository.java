
package tea4life.user_service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tea4life.user_service.model.User;

import java.util.Optional;

/**
 * Admin 2/3/2026
 *
 **/
@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {

    boolean existsByKeycloakId(String keycloakId);

    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);

}
