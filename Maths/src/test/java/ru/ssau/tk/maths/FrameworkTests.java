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

        AppUser u = userRepository.save(new AppUser("testuser1", "hash", "USER"));

        Function f = functionRepository.save(
                new Function("sine", "sin(x)", u)
        );

        List<Point> points =
                functionService.getOrComputePoints(f.getId(), -3.14, 3.14, 0.5);

        assertNotNull(points);
        assertTrue(points.size() > 0);

        List<Point> points2 =
                functionService.getOrComputePoints(f.getId(), -3.14, 3.14, 0.5);

        assertEquals(points.size(), points2.size());

        List<Point> recomputed =
                functionService.recomputePoints(f.getId(), -1.0, 1.0, 0.25);

        assertTrue(recomputed.size() > 0);
        assertNotEquals(points.size(), recomputed.size());
    }
}
