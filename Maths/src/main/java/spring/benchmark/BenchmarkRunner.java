package spring.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BenchmarkRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(BenchmarkRunner.class);
    private final SpringDataBenchmark springDataBenchmark;
    private final JdbcBenchmark jdbcBenchmark;

    public BenchmarkRunner(SpringDataBenchmark springDataBenchmark, JdbcBenchmark jdbcBenchmark) {
        this.springDataBenchmark = springDataBenchmark;
        this.jdbcBenchmark = jdbcBenchmark;
    }
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting performance comparison benchmark...");

        List<SpringDataBenchmark.BenchmarkResult> results = new ArrayList<>();

        // Запуск Spring Data JPA бенчмарка
        var springResult = springDataBenchmark.runPerformanceTest();
        results.add(springResult);

        // Запуск JDBC бенчмарка
        var jdbcResult = jdbcBenchmark.runPerformanceTest();
        results.add(jdbcResult);

        // Сохранение результатов в CSV
        saveResultsToCsv(results, "performance_comparison.csv");

        log.info("=== FINAL COMPARISON RESULTS ===");
        log.info("Spring Data JPA: {}", springResult);
        log.info("JDBC: {}", jdbcResult);
        log.info("Results saved to: performance_comparison.csv");

        // Анализ результатов
        analyzeResults(springResult, jdbcResult);
    }

    private void saveResultsToCsv(List<SpringDataBenchmark.BenchmarkResult> results, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Заголовок CSV
            writer.write("Approach,InsertTime(ms),SelectTime(ms),UpdateTime(ms),DeleteTime(ms)\n");

            // Данные
            for (var result : results) {
                writer.write(result.toCsv() + "\n");
            }

            log.info("CSV file created: {}", filename);
        } catch (Exception e) {
            log.error("Error saving results to CSV", e);
        }
    }
    private void analyzeResults(SpringDataBenchmark.BenchmarkResult springResult,
                                SpringDataBenchmark.BenchmarkResult jdbcResult) {
        log.info("=== PERFORMANCE ANALYSIS ===");

        compareOperation("INSERT", springResult.insertTime, jdbcResult.insertTime);
        compareOperation("SELECT", springResult.selectTime, jdbcResult.selectTime);
        compareOperation("UPDATE", springResult.updateTime, jdbcResult.updateTime);
        compareOperation("DELETE", springResult.deleteTime, jdbcResult.deleteTime);

        long springTotal = springResult.insertTime + springResult.selectTime +
                springResult.updateTime + springResult.deleteTime;
        long jdbcTotal = jdbcResult.insertTime + jdbcResult.selectTime +
                jdbcResult.updateTime + jdbcResult.deleteTime;

        log.info("TOTAL TIME - Spring Data JPA: {}ms, JDBC: {}ms", springTotal, jdbcTotal);
        log.info("JDBC is {}% faster overall",
                ((springTotal - jdbcTotal) * 100) / springTotal);
    }
    private void compareOperation(String operation, long springTime, long jdbcTime) {
        double difference = ((double)(springTime - jdbcTime) / springTime) * 100;
        String faster = springTime > jdbcTime ? "JDBC" : "Spring Data JPA";
        log.info("{}: Spring Data JPA {}ms vs JDBC {}ms ({} is {:.1f}% faster)",
                operation, springTime, jdbcTime, faster, Math.abs(difference));
    }
}