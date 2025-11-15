package ru.ssau.tk.maths.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.maths.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
