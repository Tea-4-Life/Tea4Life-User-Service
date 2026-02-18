package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UpsertPermissionRequest;
import tea4life.user_service.dto.response.PermissionResponse;
import tea4life.user_service.model.Permission;
import tea4life.user_service.repository.PermissionRepository;
import tea4life.user_service.service.PermissionService;
import tea4life.user_service.utils.PermissionMapper;

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

    PermissionRepository permissionRepository;

    @Override
    public void createPermission(UpsertPermissionRequest upsertPermissionRequest) {
        String name = upsertPermissionRequest.name().toUpperCase();

        if (permissionRepository.existsByName(name))
            throw new DataIntegrityViolationException("Tên quyền này đã tồn tại");

        Permission permission = PermissionMapper.mapToPermission(upsertPermissionRequest);
        permissionRepository.save(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<@NonNull PermissionResponse> findAllPermissions(Pageable pageable) {
        return permissionRepository
                .findAll(pageable)
                .map(PermissionMapper::mapToPermissionResponse);
    }

    @Override
    public void updatePermission(UpsertPermissionRequest upsertPermissionRequest, Long id) {
        Permission permission = permissionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền"));

        String name = upsertPermissionRequest.name().toUpperCase();

        if (permissionRepository.existsByNameAndIdNot(name, permission.getId()))
            throw new DataIntegrityViolationException("Tên quyền này đã tồn tại");

        PermissionMapper.updatePermissionFromRequest(permission, upsertPermissionRequest);

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
