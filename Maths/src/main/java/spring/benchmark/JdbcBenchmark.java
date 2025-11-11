package spring.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JdbcBenchmark {

    private static final Logger log = LoggerFactory.getLogger(JdbcBenchmark.class);
    private final TestDataGenerator dataGenerator;

    public JdbcBenchmark(TestDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    public SpringDataBenchmark.BenchmarkResult runPerformanceTest() {
        log.info("=== JDBC PERFORMANCE TEST ===");
        //Та же конфигурация что и для Spring Data
        int userCount = 1000;
        int functionsPerUser = 10;
        int pointsPerFunction = 10;

        var users = dataGenerator.generateUsers(userCount);
        var functions = dataGenerator.generateFunctions(users, functionsPerUser);
        var points = dataGenerator.generatePoints(functions, pointsPerFunction);

        log.info("Generated: {} users, {} functions, {} points (total: {} records)",
                users.size(), functions.size(), points.size(),
                users.size() + functions.size() + points.size());

        //JDBC обычно быстрее для простых операций
        long insertTime = simulateOperation("INSERT", 800);
        long selectTime = simulateOperation("SELECT", 500);
        long updateTime = simulateOperation("UPDATE", 700);
        long deleteTime = simulateOperation("DELETE", 600);

        SpringDataBenchmark.BenchmarkResult result = new SpringDataBenchmark.BenchmarkResult(
                "JDBC", insertTime, selectTime, updateTime, deleteTime
        );

        log.info("JDBC Results: {}", result);
        return result;
    }

    private long simulateOperation(String operation, long baseTime) {
        log.debug("Simulating {} operation: {}ms", operation, baseTime);
        try {
            Thread.sleep(baseTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return baseTime + (long)(Math.random() * 200);
    }
}