package repository.performance;

import dao.impl.UserDaoImpl;
import model.User;
import org.junit.jupiter.api.*;
import java.io.FileWriter;
import java.sql.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PerformanceTest {
    private static Connection conn;
    private static UserDaoImpl userDao;

    private static final int RECORD_COUNT = 10000;

    @BeforeAll
    static void setup() throws Exception {
        conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/lab5",
                "labuser",
                "labpass");
        userDao = new UserDaoImpl(conn);

        // Очистка таблицы перед тестами
        try (Statement st = conn.createStatement()) {
            st.execute("DELETE FROM app_user");
        }
    }

    @Test
    @Order(1)
    void manualPerformanceTest() {
        System.out.println("\n=== 📊 MANUAL JDBC PERFORMANCE TEST START ===");

        // 1️⃣ INSERT
        long insertStart = System.currentTimeMillis();
        for (int i = 0; i < RECORD_COUNT; i++) {
            userDao.insert(new User(0, "dao_user_" + i, "hash_" + i));
        }
        long insertEnd = System.currentTimeMillis();

        // 2️⃣ SELECT ALL
        long selectAllStart = System.currentTimeMillis();
        List<User> allUsers = userDao.findAll();
        long selectAllEnd = System.currentTimeMillis();

        // 3️⃣ SELECT BY ID
        long selectByIdStart = System.currentTimeMillis();
        for (int i = 1; i <= 100; i++) {
            userDao.findById(i);
        }
        long selectByIdEnd = System.currentTimeMillis();

        // 4️⃣ UPDATE
        long updateStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            User u = allUsers.get(i);
            u.setUsername("dao_updated_" + i);
            userDao.update(u);
        }
        long updateEnd = System.currentTimeMillis();

        // 5️⃣ DELETE
        long deleteStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            userDao.delete(allUsers.get(i).getId());
        }
        long deleteEnd = System.currentTimeMillis();

        // ✅ Вывод в консоль
        System.out.println("\n--- Manual JDBC Results (ms) ---");
        System.out.printf("INSERT %d records: %d ms%n", RECORD_COUNT, (insertEnd - insertStart));
        System.out.printf("SELECT ALL %d records: %d ms%n", allUsers.size(), (selectAllEnd - selectAllStart));
        System.out.printf("SELECT BY ID (100 records): %d ms%n", (selectByIdEnd - selectByIdStart));
        System.out.printf("UPDATE 100 records: %d ms%n", (updateEnd - updateStart));
        System.out.printf("DELETE 100 records: %d ms%n", (deleteEnd - deleteStart));

        // ✅ Автоматическая запись таблицы в CSV
        try (FileWriter writer = new FileWriter("performance_results.csv")) {
            writer.write("Operation,Records,Time (ms)\n");
            writer.write("INSERT," + RECORD_COUNT + "," + (insertEnd - insertStart) + "\n");
            writer.write("SELECT_ALL," + allUsers.size() + "," + (selectAllEnd - selectAllStart) + "\n");
            writer.write("SELECT_BY_ID,100," + (selectByIdEnd - selectByIdStart) + "\n");
            writer.write("UPDATE,100," + (updateEnd - updateStart) + "\n");
            writer.write("DELETE,100," + (deleteEnd - deleteStart) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n✅ Manual results written to performance_results.csv");
        System.out.println("=== 📊 MANUAL JDBC PERFORMANCE TEST END ===\n");
    }

    @AfterAll
    static void cleanup() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}