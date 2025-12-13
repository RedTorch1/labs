package ru.ssau.tk.maths;

import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.repository.AppUserRepository;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.service.FunctionService;
import ru.ssau.tk.maths.service.SearchService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

@SpringBootTest(properties = "spring.profiles.active=test")
public class PerformanceTests {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private SearchService searchService;

    @Test
    public void benchmarkSearches() throws Exception {

        AppUser u = userRepository.save(new AppUser("perf_user", "hash", "USER"));

        Function f = functionRepository.save(
                new Function("perf_func", "sin(x)", u)
        );

        functionService.getOrComputePoints(f.getId(), -5, 5, 1);

        Path dir = Path.of("target", "performance");
        Files.createDirectories(dir);

        Path results = dir.resolve("results.csv");
        FileWriter fw = new FileWriter(results.toFile(), false);

        fw.write("algorithm,time_ms\n");

        Instant t1 = Instant.now();
        searchService.dfs(u);
        fw.write("DFS," + Duration.between(t1, Instant.now()).toMillis() + "\n");

        t1 = Instant.now();
        searchService.bfs(u);
        fw.write("BFS," + Duration.between(t1, Instant.now()).toMillis() + "\n");

        fw.close();
    }
}
