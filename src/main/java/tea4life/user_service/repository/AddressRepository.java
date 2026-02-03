package tea4life.user_service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tea4life.user_service.model.Address;

/**
 * Admin 2/3/2026
 *
 **/
@Repository
public interface AddressRepository extends JpaRepository<@NonNull Address, @NonNull Long> {
}
