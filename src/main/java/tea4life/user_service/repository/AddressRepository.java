package tea4life.user_service.repository;

import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tea4life.user_service.model.Address;

import java.util.List;
import java.util.Optional;

/**
 * Admin 2/3/2026
 *
 **/
@Repository
public interface AddressRepository extends JpaRepository<@NonNull Address, @NonNull Long> {
    long countByUserId(Long userId);

    List<Address> findAllByUserIdOrderByIsDefaultDescCreatedAtDesc(Long userId);

    Optional<Address> findByIdAndUserId(Long id, Long userId);
}
