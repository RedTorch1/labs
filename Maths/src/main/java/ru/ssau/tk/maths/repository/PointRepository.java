package ru.ssau.tk.maths.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.maths.entity.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    // Найти все точки по function.id, отсортированные по полю xValue (именно xValue, не XValue!)
    List<Point> findByFunction_IdOrderByXValueAsc(Long functionId);

    // Найти точку по function.id и xValue
    Optional<Point> findByFunction_IdAndXValue(Long functionId, BigDecimal xValue);

    // Удалить все точки по id функции
    void deleteByFunction_Id(Long functionId);
}
