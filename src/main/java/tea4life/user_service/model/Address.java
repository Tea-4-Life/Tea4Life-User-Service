package tea4life.user_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tea4life.user_service.config.database.SnowflakeGenerated;
import tea4life.user_service.model.base.BaseEntity;
import tea4life.user_service.model.constant.AddressType;

/**
 * Admin 2/3/2026
 *
 **/
@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseEntity {

    @SnowflakeGenerated
    @Id
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String receiverName;
    String phone;

    String province; // Tỉnh/Thành phố
    String ward;     // Phường/Xã
    String detail;   // Số nhà, tên đường

    @Column(name = "latitude", nullable = false)
    Double latitude;

    @Column(name = "longitude", nullable = false)
    Double longitude;

    @Enumerated(EnumType.STRING)
    AddressType addressType;

    @Column(name = "is_default", nullable = false)
    boolean isDefault = false;

}
