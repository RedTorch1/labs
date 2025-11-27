package ru.ssau.tk.maths.util;

import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.entity.Point;
import ru.ssau.tk.maths.repository.AppUserRepository;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.repository.PointRepository;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
@Profile("!test") // <===== КЛЮЧЕВАЯ СТРОКА
public class DataGenerator {

    private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);

    private final AppUserRepository userRepository;
    private final FunctionRepository functionRepository;
    private final PointRepository pointRepository;

    public DataGenerator(AppUserRepository userRepository,
                         FunctionRepository functionRepository,
                         PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.functionRepository = functionRepository;
        this.pointRepository = pointRepository;
    }

    @Transactional
    public void generateBulkData() {

        log.info("=== Генерация данных (боевой режим) ===");

        Random rnd = new Random();
        List<AppUser> users = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            String role = (i % 20 == 0 ? "ADMIN" : "USER");
            users.add(new AppUser("user_" + i, "hash" + i, role));
        }

        users = userRepository.saveAll(users);

        String[] exprs = {
                "sin(x)", "cos(x)", "x*x", "x*x+3*x-2", "log(abs(x)+1)", "exp(sin(x))"
        };

        List<Function> funcs = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            AppUser owner = users.get(rnd.nextInt(users.size()));
            funcs.add(new Function("func_" + i, exprs[rnd.nextInt(exprs.length)], owner));
        }

        funcs = functionRepository.saveAll(funcs);

        List<Point> points = new ArrayList<>();

        for (Function f : funcs) {

            Expression expr = new ExpressionBuilder(f.getExpression()).variable("x").build();

            for (double x = -10; x <= 10; x += 0.25) {

                double y;
                try {
                    y = expr.setVariable("x", x).evaluate();
                } catch (Exception ex) {
                    continue;
                }

                points.add(new Point(
                        f,
                        BigDecimal.valueOf(x).setScale(4, RoundingMode.HALF_UP),
                        BigDecimal.valueOf(y).setScale(4, RoundingMode.HALF_UP)
                ));
            }
        }

        pointRepository.saveAll(points);

        log.info("=== Генерация завершена ===");
    }
}
