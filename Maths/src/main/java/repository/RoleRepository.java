package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
