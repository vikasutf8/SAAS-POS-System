package com.pm.saaspossystem.model;

import com.pm.saaspossystem.domain.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone"),
                @Index(name = "idx_user_store", columnList = "store_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank @Size(max = 100)
    @Column(nullable = false)
    private String fullName;

    @NotBlank @Size(min = 8, max = 100)
    @Column(nullable = false)
    private String password;

    @NotBlank @Pattern(regexp = "^[0-9]{10,15}$")
    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @NotBlank @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    /**
     * Populated when this user is assigned as Store Manager.
     * Null for Admin, Branch Manager, Cashier.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private UserRole role;




    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
//        this.isActive =true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
