package com.dio.pix.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;
@Entity
@Table(name = "receivers")
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receiver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pix_key", nullable = false, unique = true)
    private String pixKey;
    @Enumerated(EnumType.STRING)
    @Column(name = "pix_key_type", nullable = false)
    private PixKeyType pixKeyType;
    @Column(nullable = false, length = 25)
    private String name;
    @Column(nullable = false, length = 15)
    private String city;
    @Column(name = "merchant_category_code", length = 4)
    private String merchantCategoryCode;
    @Column(name = "is_default")
    private Boolean isDefault;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (merchantCategoryCode == null) {
            merchantCategoryCode = "0000";
        }
        if (isDefault == null) {
            isDefault = false;
        }
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
