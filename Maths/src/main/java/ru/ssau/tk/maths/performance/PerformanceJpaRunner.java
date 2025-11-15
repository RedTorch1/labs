package ru.ssau.tk.maths.performance;

import org.springframework.stereotype.Component;
import ru.ssau.tk.maths.entity.UserEntity;
import ru.ssau.tk.maths.repository.UserRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class PerformanceJpaRunner {

    private final UserRepository userRepository;

    public PerformanceJpaRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void runAndSave(int count) {

        userRepository.deleteAll();

        long insertStart = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            UserEntity user = new UserEntity("user_" + i, "pass_" + i);
            userRepository.save(user);
        }
        long insertEnd = System.currentTimeMillis();

        // SELECT (полное чтение всех записей)
        long selectStart = System.currentTimeMillis();
        var allUsers = userRepository.findAll();
        long selectEnd = System.currentTimeMillis();

        // UPDATE (обновляем 1000 записей)
        long updateStart = System.currentTimeMillis();
        int updated = 0;
        for (UserEntity u : allUsers) {
            if (updated >= 1000) break;
            u.setPassword("updated_pass");
            userRepository.save(u);
            updated++;
        }
        long updateEnd = System.currentTimeMillis();

        // DELETE (удаляем 1000)
        long deleteStart = System.currentTimeMillis();
        int deleted = 0;
        for (UserEntity u : userRepository.findAll()) {
            if (deleted >= 1000) break;
            userRepository.delete(u);
            deleted++;
        }
        long deleteEnd = System.currentTimeMillis();

        long insertTime = insertEnd - insertStart;
        long selectTime = selectEnd - selectStart;
        long updateTime = updateEnd - updateStart;
        long deleteTime = deleteEnd - deleteStart;

        saveToCSV(insertTime, selectTime, updateTime, deleteTime, count);
    }

    private void saveToCSV(long insert, long select, long update, long delete, int count) {
        String fileName = "performance_jpa.csv";

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.append(LocalDateTime.now().toString()).append(",");
            writer.append(String.valueOf(count)).append(",");
            writer.append(insert + " ms").append(",");
            writer.append(select + " ms").append(",");
            writer.append(update + " ms").append(",");
            writer.append(delete + " ms").append("\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}