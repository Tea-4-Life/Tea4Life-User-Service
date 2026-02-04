package tea4life.user_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.user_service.generator.SnowflakeGenerated;
import tea4life.user_service.model.base.BaseEntity;
import tea4life.user_service.model.constant.Gender;

import java.time.LocalDate;

/**
 * Admin 2/3/2026
 *
 **/
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @SnowflakeGenerated
    @Id
    Long id;

    @Column(name = "keycloak_id", unique = true, nullable = false)
    String keycloakId;

    @Column(nullable = false, unique = true)
    String email;

    @Column(length = 50)
    String fullName;

    String avatarUrl;

    @Column(length = 15)
    String phone;

    LocalDate dob;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    Role role;

}
