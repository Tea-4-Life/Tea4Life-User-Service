package tea4life.user_service.service;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UpsertRoleRequest;
import tea4life.user_service.dto.response.RoleResponse;

import java.util.List;

/**
 * Admin 2/18/2026
 *
 **/
public interface RoleService {
    void createRole(UpsertRoleRequest upsertRoleRequest);

    @Transactional(readOnly = true)
    Page<@NonNull RoleResponse> findAllRoles(Pageable pageable);

    @Transactional(readOnly = true)
    List<RoleResponse> findAllRoles();

    RoleResponse findById(Long id);

    void updateRole(UpsertRoleRequest upsertRoleRequest, Long id);

    void deleteRoleById(Long id);
}
