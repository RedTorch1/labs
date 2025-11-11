package spring.benchmark;

import spring.entity.AppUser;
import spring.entity.Function;
import spring.entity.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TestDataGenerator {

    private static final Logger log = LoggerFactory.getLogger(TestDataGenerator.class);
    private final Random random = new Random();
    public List<AppUser> generateUsers(int count) {
        log.info("Generating {} test users", count);
        List<AppUser> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            AppUser user = new AppUser();
            user.setUsername("user_" + i);
            user.setPasswordHash("hash_" + i);
            user.setEmail("user" + i + "@mail.ru");
            users.add(user);
        }

        return users;
    }
    public List<Function> generateFunctions(List<AppUser> users, int functionsPerUser) {
        log.info("Generating {} functions per user", functionsPerUser);
        List<Function> functions = new ArrayList<>();
        String[] functionTypes = {"linear", "quadratic", "sinus", "exponential"};

        for (AppUser user : users) {
            for (int j = 0; j < functionsPerUser; j++) {
                Function function = new Function();
                function.setName(functionTypes[j % functionTypes.length] + "_" + j);
                function.setExpression(generateExpression(functionTypes[j % functionTypes.length]));
                function.setUserId(user.getId());
                functions.add(function);
            }
        }

        return functions;
    }
    public List<Point> generatePoints(List<Function> functions, int pointsPerFunction) {
        log.info("Generating {} points per function", pointsPerFunction);
        List<Point> points = new ArrayList<>();

        for (Function function : functions) {
            for (int k = 0; k < pointsPerFunction; k++) {
                Point point = new Point();
                point.setFunctionId(function.getId());
                point.setXValue(random.nextDouble() * 100);
                point.setYValue(random.nextDouble() * 100);
                points.add(point);
            }
        }

        return points;
    }
    private String generateExpression(String type) {
        return switch (type) {
            case "linear" -> "2*x + 1";
            case "quadratic" -> "x^2 + 2*x + 1";
            case "sinus" -> "sin(x)";
            case "exponential" -> "exp(x)";
            default -> "x";
        };
    }
}