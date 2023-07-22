package au.com.jc.authservice.repository;

import au.com.jc.authservice.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    List<AuthUser> findByUsername(String username);
}
