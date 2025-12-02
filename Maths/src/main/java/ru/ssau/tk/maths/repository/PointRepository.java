package ru.ssau.tk.maths.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.maths.entity.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByFunction_IdOrderByXvalueAsc(Long functionId);

    Optional<Point> findByFunction_IdAndXvalue(Long functionId, BigDecimal xValue);


    // Удалить все точки по id функции
    void deleteByFunction_Id(Long functionId);
}
