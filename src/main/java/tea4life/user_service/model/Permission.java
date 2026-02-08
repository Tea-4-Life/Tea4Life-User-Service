package tea4life.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.user_service.config.database.SnowflakeGenerated;
import tea4life.user_service.model.base.BaseEntity;

/**
 * Admin 2/3/2026
 *
 **/
@Entity
@Table(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {

    @Id
    @SnowflakeGenerated
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    String description;

}
