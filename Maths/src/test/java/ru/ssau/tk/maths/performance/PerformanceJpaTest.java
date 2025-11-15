package ru.ssau.tk.maths.performance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PerformanceJpaTest {

    @Autowired PerformanceJpaRunner runner;

    @Test
    void runPerformance() throws Exception {
        runner.runAndSave(10000);
    }
}
