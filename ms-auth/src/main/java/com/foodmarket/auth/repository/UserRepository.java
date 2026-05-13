package com.foodmarket.auth.repository;
import com.foodmarket.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Genera: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
    // Genera: SELECT COUNT(*) > 0 FROM users WHERE email = ?
    boolean existsByEmail(String email);
}
