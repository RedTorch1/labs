package performance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PerformanceJpaTest {

    @Autowired PerformanceJpaRunner runner;

    @Test
    void runPerformance() throws Exception {
        // use 10000 for real test; reduce to 2000 for CI or local quick test
        runner.runAndSave(5000);
    }
}
