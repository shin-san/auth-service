package au.com.jc.authservice.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_user_id_generator")
//    @SequenceGenerator(name = "auth_user_id_generator", sequenceName = "auth_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

}
