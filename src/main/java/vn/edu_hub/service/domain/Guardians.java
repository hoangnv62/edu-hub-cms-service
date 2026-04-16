package vn.edu_hub.service.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guardians")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Guardians extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "fullname", nullable = false)
    String fullName;

    @Column(name = "phone", nullable = false)
    String phone;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "address")
    String address;

    @Column(name = "image_url")
    String imageUrl;
}
