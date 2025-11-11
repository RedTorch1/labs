package spring.benchmark;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceComparisonTest {

    private TestDataGenerator dataGenerator;
    private SpringDataBenchmark springDataBenchmark;
    private JdbcBenchmark jdbcBenchmark;

    @BeforeEach
    void setUp() {
        dataGenerator = new TestDataGenerator();
        springDataBenchmark = new SpringDataBenchmark(dataGenerator);
        jdbcBenchmark = new JdbcBenchmark(dataGenerator);
    }

    @Test
    void testPerformanceComparison() throws IOException {
        System.out.println("=== STARTING PERFORMANCE COMPARISON TEST ===");

        List<SpringDataBenchmark.BenchmarkResult> results = new ArrayList<>();

        // Запуск Spring Data JPA бенчмарка
        var springResult = springDataBenchmark.runPerformanceTest();
        results.add(springResult);

        // Запуск JDBC бенчмарка
        var jdbcResult = jdbcBenchmark.runPerformanceTest();
        results.add(jdbcResult);

        // Сохранение результатов в CSV
        saveResultsToCsv(results, "performance_comparison.csv");

        // Вывод результатов в консоль
        System.out.println("=== RESULTS ===");
        System.out.println("Spring Data JPA: " + springResult);
        System.out.println("JDBC: " + jdbcResult);
        System.out.println("CSV file created: performance_comparison.csv");

        // Анализ
        analyzeResults(springResult, jdbcResult);
    }

    private void saveResultsToCsv(List<SpringDataBenchmark.BenchmarkResult> results, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Approach,InsertTime(ms),SelectTime(ms),UpdateTime(ms),DeleteTime(ms)\n");

            for (var result : results) {
                writer.write(result.toCsv() + "\n");
            }
        }
    }

    private void analyzeResults(SpringDataBenchmark.BenchmarkResult springResult,
                                SpringDataBenchmark.BenchmarkResult jdbcResult) {
        System.out.println("=== PERFORMANCE ANALYSIS ===");

        compareOperation("INSERT", springResult.insertTime, jdbcResult.insertTime);
        compareOperation("SELECT", springResult.selectTime, jdbcResult.selectTime);
        compareOperation("UPDATE", springResult.updateTime, jdbcResult.updateTime);
        compareOperation("DELETE", springResult.deleteTime, jdbcResult.deleteTime);

        long springTotal = springResult.insertTime + springResult.selectTime +
                springResult.updateTime + springResult.deleteTime;
        long jdbcTotal = jdbcResult.insertTime + jdbcResult.selectTime +
                jdbcResult.updateTime + jdbcResult.deleteTime;

        System.out.println("TOTAL TIME - Spring Data JPA: " + springTotal + "ms, JDBC: " + jdbcTotal + "ms");
        System.out.println("JDBC is " + ((springTotal - jdbcTotal) * 100 / springTotal) + "% faster overall");
    }

    private void compareOperation(String operation, long springTime, long jdbcTime) {
        double difference = ((double)(springTime - jdbcTime) / springTime) * 100;
        String faster = springTime > jdbcTime ? "JDBC" : "Spring Data JPA";
        System.out.println(operation + ": Spring Data JPA " + springTime + "ms vs JDBC " + jdbcTime +
                "ms (" + faster + " is " + Math.abs(difference) + "% faster)");
    }
}