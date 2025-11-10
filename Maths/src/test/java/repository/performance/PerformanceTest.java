package repository.performance;

import dao.impl.*;
import model.User;
import repository.impl.*;
import org.junit.jupiter.api.*;
import java.io.FileWriter;
import java.sql.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PerformanceTest {
    private static Connection conn;
    private static UserDaoImpl userDao;
    private static UserRepositoryImpl userRepo;

    private static final int RECORD_COUNT = 10000;

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");
        userDao = new UserDaoImpl(conn);
        userRepo = new UserRepositoryImpl(userDao);
    }

    @Test
    @Order(1)
    void compareDaoAndRepo() {
        System.out.println("\n=== 📊 PERFORMANCE TEST START ===");

        // 1️⃣ INSERT
        long daoInsertStart = System.currentTimeMillis();
        for (int i = 0; i < RECORD_COUNT; i++) {
            userDao.insert(new User(0, "dao_user_" + i, "hash_" + i));
        }
        long daoInsertEnd = System.currentTimeMillis();

        long repoInsertStart = System.currentTimeMillis();
        for (int i = 0; i < RECORD_COUNT; i++) {
            userRepo.create("repo_user_" + i, "hash_" + i);
        }
        long repoInsertEnd = System.currentTimeMillis();

        // 2️⃣ SELECT
        long daoSelectStart = System.currentTimeMillis();
        List<User> daoUsers = userDao.findAll();
        long daoSelectEnd = System.currentTimeMillis();

        long repoSelectStart = System.currentTimeMillis();
        List<User> repoUsers = userRepo.findAll();
        long repoSelectEnd = System.currentTimeMillis();

        // 3️⃣ UPDATE
        long daoUpdateStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) { // обновим только первые 100 — иначе будет очень долго
            User u = daoUsers.get(i);
            u.setUsername("dao_updated_" + i);
            userDao.update(u);
        }
        long daoUpdateEnd = System.currentTimeMillis();

        long repoUpdateStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            User u = repoUsers.get(i);
            u.setUsername("repo_updated_" + i);
            userRepo.create(u.getUsername(), u.getPasswordHash()); // имитация update через repo
        }
        long repoUpdateEnd = System.currentTimeMillis();

        // 4️⃣ DELETE
        long daoDeleteStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            userDao.delete(daoUsers.get(i).getId());
        }
        long daoDeleteEnd = System.currentTimeMillis();

        long repoDeleteStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            userRepo.delete(repoUsers.get(i).getId());
        }
        long repoDeleteEnd = System.currentTimeMillis();

        // ✅ Вывод в консоль
        System.out.println("\n--- Results (ms) ---");
        System.out.printf("INSERT: DAO=%d | REPO=%d%n", (daoInsertEnd - daoInsertStart), (repoInsertEnd - repoInsertStart));
        System.out.printf("SELECT: DAO=%d | REPO=%d%n", (daoSelectEnd - daoSelectStart), (repoSelectEnd - repoSelectStart));
        System.out.printf("UPDATE: DAO=%d | REPO=%d%n", (daoUpdateEnd - daoUpdateStart), (repoUpdateEnd - repoUpdateStart));
        System.out.printf("DELETE: DAO=%d | REPO=%d%n", (daoDeleteEnd - daoDeleteStart), (repoDeleteEnd - repoDeleteStart));

        // ✅ Автоматическая запись таблицы в CSV
        try (FileWriter writer = new FileWriter("performance_results.csv")) {
            writer.write("Operation,Records,DAO (ms),Repository (ms)\n");
            writer.write("INSERT," + RECORD_COUNT + "," + (daoInsertEnd - daoInsertStart) + "," + (repoInsertEnd - repoInsertStart) + "\n");
            writer.write("SELECT," + RECORD_COUNT + "," + (daoSelectEnd - daoSelectStart) + "," + (repoSelectEnd - repoSelectStart) + "\n");
            writer.write("UPDATE,100," + (daoUpdateEnd - daoUpdateStart) + "," + (repoUpdateEnd - repoUpdateStart) + "\n");
            writer.write("DELETE,100," + (daoDeleteEnd - daoDeleteStart) + "," + (repoDeleteEnd - repoDeleteStart) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n✅ Results written to performance_results.csv");
        System.out.println("=== 📊 PERFORMANCE TEST END ===\n");
    }

    @AfterAll
    static void cleanup() throws Exception {
        conn.close();
    }
}
