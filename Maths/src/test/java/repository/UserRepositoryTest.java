package repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    void testCrud() {
        UserEntity u = new UserEntity("tuser", "pass");
        UserEntity saved = userRepository.save(u);
        assertNotNull(saved.getId());

        UserEntity found = userRepository.findById(saved.getId()).orElseThrow();
        assertEquals("tuser", found.getUsername());

        found.setPassword("new");
        userRepository.save(found);

        userRepository.delete(found);
        assertFalse(userRepository.findById(found.getId()).isPresent());
    }
}
