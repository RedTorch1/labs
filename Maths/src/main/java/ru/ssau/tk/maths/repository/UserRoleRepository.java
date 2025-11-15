package ru.ssau.tk.maths.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.maths.entity.UserRoleEntity;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {}
