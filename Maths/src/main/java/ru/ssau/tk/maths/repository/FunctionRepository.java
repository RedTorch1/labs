package ru.ssau.tk.maths.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import ru.ssau.tk.maths.entity.Function;


public interface FunctionRepository extends JpaRepository<Function, Long> {
    List<Function> findByUserId(Long userId);
}
