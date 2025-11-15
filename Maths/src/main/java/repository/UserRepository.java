package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import entity.UserEntity;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByUsernameContainingIgnoreCase(String usernamePart);
    List<UserEntity> findByUsername(String username);
}
