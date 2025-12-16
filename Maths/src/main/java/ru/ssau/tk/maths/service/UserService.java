package ru.ssau.tk.maths.service;

import ru.ssau.tk.maths.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private static final Map<Long, User> users = new ConcurrentHashMap<>();
    private static long nextId = 1;

    static {
        // Тестовый пользователь
        User admin = new User("admin", "admin123");
        admin.setId(1L);
        users.put(1L, admin);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public static List<User> findByUsername(String username) {
        List<User> result = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                result.add(user);
            }
        }
        return result;
    }

    public static User createUser(User user) throws Exception {
        // Проверка уникальности
        List<User> existing = findByUsername(user.getUsername());
        if (!existing.isEmpty()) {
            throw new Exception("Пользователь с логином '" + user.getUsername() + "' уже существует");
        }

        // Создаем
        user.setId(nextId++);
        users.put(user.getId(), user);
        System.out.println("✅ Создан пользователь: " + user);
        return user;
    }

    public static User findById(Long id) {
        return users.get(id);
    }
}
