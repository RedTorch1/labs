package ru.ssau.tk.maths;

import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.entity.Point;
import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.repository.AppUserRepository;
import ru.ssau.tk.maths.service.FunctionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FrameworkTests {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private FunctionService functionService;

    @Test
    public void testComputePointsFlow() {
        // 1) создать пользователя
        AppUser u = new AppUser("testuser1", "hash", "admin");
        u = userRepository.save(u);

        // 2) создать функцию пользователя (пример: sin(x))
        Function f = new Function("sine", "sin(x)", u);
        f = functionRepository.save(f);

        // 3) запросить точки: их нет => сервис их вычислит и сохранит
        List<Point> points = functionService.getOrComputePoints(f.getId(), -3.14, 3.14, 0.5);

        assertNotNull(points);
        assertTrue(points.size() > 0, "Ожидаются вычисленные точки");

        // 4) повторный запрос — возвращает уже существующие точки (не вычисляет заново)
        List<Point> points2 = functionService.getOrComputePoints(f.getId(), -3.14, 3.14, 0.5);
        assertEquals(points.size(), points2.size());

        // 5) пересчёт точек (удалить/создать заново)
        List<Point> recomputed = functionService.recomputePoints(f.getId(), -1.0, 1.0, 0.25);
        assertTrue(recomputed.size() > 0);
        assertNotEquals(points.size(), recomputed.size());
    }
}
