package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {}
