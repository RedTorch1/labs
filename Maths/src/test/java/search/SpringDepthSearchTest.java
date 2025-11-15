package search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringDepthSearchTest {

    @Autowired UserRepository userRepository;

    @Test
    void smokeTest() {
        SpringDepthSearch search = new SpringDepthSearch(userRepository);
        // if DB empty, returns empty list; that's acceptable
        assertNotNull(search.searchFromUsername("non-existing"));
    }
}
