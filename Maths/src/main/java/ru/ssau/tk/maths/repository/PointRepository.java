package ru.ssau.tk.maths.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.maths.entity.Point;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByFunctionIdOrderByXValueAsc(Long functionId);

    Optional<Point> findByFunctionIdAndXValue(Long functionId, BigDecimal xValue);

    void deleteByFunctionId(Long functionId);
}
