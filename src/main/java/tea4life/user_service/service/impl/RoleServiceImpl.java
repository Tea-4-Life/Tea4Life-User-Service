package tea4life.user_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tea4life.user_service.dto.request.UpsertRoleRequest;
import tea4life.user_service.dto.response.RoleResponse;
import tea4life.user_service.model.Permission;
import tea4life.user_service.model.Role;
import tea4life.user_service.repository.PermissionRepository;
import tea4life.user_service.repository.RoleRepository;
import tea4life.user_service.utils.RoleMapper;

import java.util.HashSet;
import java.util.Set;

/**
 * Admin 2/16/2026
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class RoleServiceImpl {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    public void createRole(UpsertRoleRequest upsertRoleRequest) {
        String name = upsertRoleRequest.name().toUpperCase();

        if (roleRepository.existsByName(name))
            throw new DataIntegrityViolationException("Tên chức vụ này đã tồn tại");

        Role role = RoleMapper.mapToRole(upsertRoleRequest);

        if (upsertRoleRequest.permissionIdList() != null && !upsertRoleRequest.permissionIdList().isEmpty()) {
            Set<Permission> permissionList = new HashSet<>(
                    permissionRepository.findAllById(upsertRoleRequest.permissionIdList())
            );

            role.setPermissions(permissionList);
        }

        roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public Page<@NonNull RoleResponse> findAllRoles(Pageable pageable) {
        return roleRepository
                .findAll(pageable)
                .map(RoleMapper::mapToRoleResponse);
    }

    public void updateRole(UpsertRoleRequest upsertRoleRequest, Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ"));

        String name = upsertRoleRequest.name().toUpperCase();
        String description = upsertRoleRequest.description() != null && !upsertRoleRequest.description().isBlank()
                ? upsertRoleRequest.description()
                : null;

        if (roleRepository.existsByNameAndIdNot(name, role.getId()))
            throw new DataIntegrityViolationException("Tên chức vụ đã tồn tại");

        role.setName(name);
        role.setDescription(description);

        if (upsertRoleRequest.permissionIdList() != null && !upsertRoleRequest.permissionIdList().isEmpty()) {
            Set<Permission> permissionList = new HashSet<>(
                    permissionRepository.findAllById(upsertRoleRequest.permissionIdList())
            );

            role.setPermissions(permissionList);
        }

        roleRepository.save(role);
    }

    public void deleteRoleById(Long id) {
        Role role = roleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chức vụ"));

        roleRepository.delete(role);
    }

}
