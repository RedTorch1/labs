package ru.ssau.tk.maths.service;

import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.entity.Point;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.repository.PointRepository;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class FunctionService {

    private final FunctionRepository functionRepository;
    private final PointRepository pointRepository;

    public FunctionService(FunctionRepository functionRepository, PointRepository pointRepository) {
        this.functionRepository = functionRepository;
        this.pointRepository = pointRepository;
    }

    public List<Function> getFunctionsByUser(Long userId) {
        return functionRepository.findByUserId(userId);
    }

    @Transactional
    public List<Point> getOrComputePoints(Long functionId, double xmin, double xmax, double step) {
        List<Point> existing = pointRepository.findByFunction_IdOrderByXValueAsc(functionId);
        if (!existing.isEmpty()) {
            return existing;
        }

        Function function = functionRepository.findById(functionId)
                .orElseThrow(() -> new IllegalArgumentException("Function not found: " + functionId));

        // собираем Expression
        // exp4j использует x как переменную: Expression e = new ExpressionBuilder("sin(x)").variable("x").build();
        Expression expr;
        try {
            expr = new ExpressionBuilder(function.getExpression())
                    .variable("x")
                    .build();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid expression: " + function.getExpression(), ex);
        }

        List<Point> computed = new ArrayList<>();
        // корректный подсчёт количества шагов с учётом погрешности
        int steps = (int) Math.round((xmax - xmin) / step);
        for (int i = 0; i <= steps; i++) {
            double x = xmin + i * step;
            expr.setVariable("x", x);
            double y;
            try {
                y = expr.evaluate();
                if (Double.isNaN(y) || Double.isInfinite(y)) {
                    // пропускаем недопустимые значения
                    continue;
                }
            } catch (Exception e) {
                // при ошибке вычисления — пропустим точку
                continue;
            }

            BigDecimal xb = BigDecimal.valueOf(x).setScale(6, RoundingMode.HALF_UP);
            BigDecimal yb = BigDecimal.valueOf(y).setScale(6, RoundingMode.HALF_UP);

            // проверяем, существует ли уже точка (на всякий случай уникальность)
            if (pointRepository.findByFunction_IdAndXValue(functionId, xb).isPresent()) {
                continue;
            }

            Point p = new Point(function, xb, yb);
            computed.add(p);
        }

        // массовое сохранение
        List<Point> saved = pointRepository.saveAll(computed);
        // вернуть сохранённые (или отсортированные существующие, если они появились параллельно)
        return pointRepository.findByFunction_IdOrderByXValueAsc(functionId);
    }

    @Transactional
    public List<Point> recomputePoints(Long functionId, double xmin, double xmax, double step) {
        pointRepository.deleteByFunction_Id(functionId);
        return getOrComputePoints(functionId, xmin, xmax, step);
    }
}
