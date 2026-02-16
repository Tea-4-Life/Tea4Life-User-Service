package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UpsertPermissionRequest;
import tea4life.user_service.dto.response.PermissionResponse;
import tea4life.user_service.model.Permission;
import tea4life.user_service.repository.PermissionRepository;
import tea4life.user_service.repository.RoleRepository;
import tea4life.user_service.service.PermissionService;

/**
 * Admin 2/16/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class PermissionServiceImpl implements PermissionService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Override
    public void createPermission(UpsertPermissionRequest upsertPermissionRequest) {
        String name = upsertPermissionRequest.name();
        String group = upsertPermissionRequest.group();
        String description = upsertPermissionRequest.description() != null && !upsertPermissionRequest.description().isBlank()
                ? upsertPermissionRequest.description()
                : null;

        Permission permission = new Permission();
        permission.setName(name);
        permission.setGroup(group);
        permission.setDescription(description);

        permissionRepository.save(permission);
    }

    @Override
    public Page<@NonNull PermissionResponse> findAllPermissions(Pageable pageable) {
        return permissionRepository
                .findAll(pageable)
                .map(permission -> new PermissionResponse(
                        permission.getId().toString(),
                        permission.getName(),
                        permission.getGroup(),
                        permission.getDescription()
                ));
    }

    @Override
    public void updatePermission(UpsertPermissionRequest upsertPermissionRequest, Long id) {

        Permission permission = permissionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền"));

        String name = upsertPermissionRequest.name();
        String group = upsertPermissionRequest.group();
        String description = upsertPermissionRequest.description() != null && !upsertPermissionRequest.description().isBlank()
                ? upsertPermissionRequest.description()
                : null;

        permission.setName(name);
        permission.setGroup(group);
        permission.setDescription(description);

        permissionRepository.save(permission);
    }

    @Override
    public void deletePermissionById(Long id) {
        Permission permission = permissionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền"));

        permissionRepository.delete(permission);
    }


}
