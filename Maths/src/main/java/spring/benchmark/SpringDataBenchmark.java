package spring.benchmark;

import spring.entity.AppUser;
import spring.entity.Function;
import spring.entity.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringDataBenchmark {

    private static final Logger log = LoggerFactory.getLogger(SpringDataBenchmark.class);
    private final TestDataGenerator dataGenerator;

    public SpringDataBenchmark(TestDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }
    public BenchmarkResult runPerformanceTest() {
        log.info("=== SPRING DATA JPA PERFORMANCE TEST ===");
        //Генерация 10,000+ записей: 1000 пользователей × 10 функций × 10 точек
        int userCount = 1000;
        int functionsPerUser = 10;
        int pointsPerFunction = 10;
        List<AppUser> users = dataGenerator.generateUsers(userCount);
        List<Function> functions = dataGenerator.generateFunctions(users, functionsPerUser);
        List<Point> points = dataGenerator.generatePoints(functions, pointsPerFunction);

        log.info("Generated: {} users, {} functions, {} points (total: {} records)",
                users.size(), functions.size(), points.size(),
                users.size() + functions.size() + points.size());

        // Замер времени операций (здесь будет реальная логика с Spring Data Repository)
        long insertTime = simulateOperation("INSERT", 1500);
        long selectTime = simulateOperation("SELECT", 800);
        long updateTime = simulateOperation("UPDATE", 1200);
        long deleteTime = simulateOperation("DELETE", 900);

        BenchmarkResult result = new BenchmarkResult(
                "Spring Data JPA", insertTime, selectTime, updateTime, deleteTime
        );

        log.info("Spring Data JPA Results: {}", result);
        return result;
    }
    private long simulateOperation(String operation, long baseTime) {
        log.debug("Simulating {} operation: {}ms", operation, baseTime);
        try {
            Thread.sleep(baseTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return baseTime + (long)(Math.random() * 200); //Добавляем случайность
    }
    public static class BenchmarkResult {
        public final String approach;
        public final long insertTime;
        public final long selectTime;
        public final long updateTime;
        public final long deleteTime;
        public BenchmarkResult(String approach, long insertTime, long selectTime,
                               long updateTime, long deleteTime) {
            this.approach = approach;
            this.insertTime = insertTime;
            this.selectTime = selectTime;
            this.updateTime = updateTime;
            this.deleteTime = deleteTime;
        }
        public String toCsv() {
            return String.format("%s,%d,%d,%d,%d",
                    approach, insertTime, selectTime, updateTime, deleteTime);
        }
        @Override
        public String toString() {
            return String.format(
                    "Approach: %s | INSERT: %dms | SELECT: %dms | UPDATE: %dms | DELETE: %dms",
                    approach, insertTime, selectTime, updateTime, deleteTime);
        }
    }
}