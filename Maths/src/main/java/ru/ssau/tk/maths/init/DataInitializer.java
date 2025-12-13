package ru.ssau.tk.maths.init;

import org.springframework.context.annotation.Profile;
import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.entity.Point;
import ru.ssau.tk.maths.repository.AppUserRepository;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.repository.PointRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FunctionRepository functionRepository;
    private final PointRepository pointRepository;

    public DataInitializer(AppUserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           FunctionRepository functionRepository,
                           PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.functionRepository = functionRepository;
        this.pointRepository = pointRepository;
    }

    @Override
    public void run(String... args) {
        // 1️⃣ Создаём администратора, если его нет
        AppUser admin;
        var optionalAdmin = userRepository.findByUsername("admin"); // Optional<AppUser>
        if (optionalAdmin.isPresent()) {
            admin = optionalAdmin.get();
        } else {
            admin = new AppUser("admin", passwordEncoder.encode("adminpass"), "ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Created default admin/adminpass");
        }

        // 2️⃣ Создаём функцию, если её нет
        Function function;
        var functions = functionRepository.findByUserId(Long.valueOf("Test Function")); // List<Function>
        if (functions.isEmpty()) {
            function = new Function("Test Function", "sin(x) + 0.5*x^2", admin);
            functionRepository.save(function);
            System.out.println("Created function: " + function.getName());
        } else {
            function = functions.get(0); // берём первую найденную функцию
        }

        // 3️⃣ Создаём точку для функции
        Point point = new Point(function,
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(2.0));
        pointRepository.save(point);
        System.out.println("Created point for function: " + function.getName());
    }
}
