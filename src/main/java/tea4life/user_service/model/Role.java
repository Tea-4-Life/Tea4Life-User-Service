package tea4life.user_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.user_service.config.database.SnowflakeGenerated;
import tea4life.user_service.model.base.BaseEntity;

import java.util.Set;

/**
 * Admin 2/3/2026
 *
 **/
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {

    @Id
    @SnowflakeGenerated
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    Set<Permission> permissions;

}
