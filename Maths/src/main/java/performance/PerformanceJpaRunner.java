package performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import entity.UserEntity;
import repository.UserRepository;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class PerformanceJpaRunner {
    private static final Logger log = LoggerFactory.getLogger(PerformanceJpaRunner.class);
    private final UserRepository userRepository;

    public PerformanceJpaRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void runAndSave(int records) throws Exception {
        log.info("Performance JPA: inserting {} records", records);

        long tInsertStart = System.currentTimeMillis();
        List<UserEntity> batch = new ArrayList<>(records);
        for (int i = 0; i < records; i++) {
            batch.add(new UserEntity("jpa_user_" + i, "p" + i));
            if (batch.size() >= 1000) {
                userRepository.saveAll(batch);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) userRepository.saveAll(batch);
        long tInsertEnd = System.currentTimeMillis();

        long tSelectStart = System.currentTimeMillis();
        userRepository.findAll();
        long tSelectEnd = System.currentTimeMillis();

        long tUpdateStart = System.currentTimeMillis();
        List<UserEntity> list = userRepository.findAll().subList(0, Math.min(100, (int)userRepository.count()));
        for (UserEntity u : list) {
            u.setPassword("updated");
            userRepository.save(u);
        }
        long tUpdateEnd = System.currentTimeMillis();

        long tDeleteStart = System.currentTimeMillis();
        // remove first 100
        list = userRepository.findAll().subList(0, Math.min(100, (int)userRepository.count()));
        for (UserEntity u : list) userRepository.delete(u);
        long tDeleteEnd = System.currentTimeMillis();

        String file = Paths.get("performance_jpa.csv").toAbsolutePath().toString();
        try (FileWriter w = new FileWriter(file)) {
            w.write("Operation,Records,TimeMs\n");
            w.write("INSERT," + records + "," + (tInsertEnd - tInsertStart) + "\n");
            w.write("SELECT," + records + "," + (tSelectEnd - tSelectStart) + "\n");
            w.write("UPDATE,100," + (tUpdateEnd - tUpdateStart) + "\n");
            w.write("DELETE,100," + (tDeleteEnd - tDeleteStart) + "\n");
        }
        log.info("Performance results written to {}", file);
    }
}
