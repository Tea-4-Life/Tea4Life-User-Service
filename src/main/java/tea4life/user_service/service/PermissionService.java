package tea4life.user_service.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UpsertPermissionRequest;
import tea4life.user_service.dto.response.PermissionResponse;

import java.util.List;

/**
 * Admin 2/16/2026
 *
 **/
public interface PermissionService {
    void createPermission(UpsertPermissionRequest upsertPermissionRequest);

    Page<@NonNull PermissionResponse> findAllPermissions(Pageable pageable);

    @Transactional(readOnly = true)
    List<PermissionResponse> findAllPermissions();

    void updatePermission(UpsertPermissionRequest upsertPermissionRequest, Long id);

    void deletePermissionById(Long id);
}
