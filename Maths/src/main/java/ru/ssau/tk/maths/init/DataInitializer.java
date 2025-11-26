package ru.ssau.tk.maths.init;

import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            var admin = new AppUser("admin", passwordEncoder.encode("adminpass"), "ROLE_ADMIN");
            userRepository.save(admin);
            LOG.info("Created default admin/adminpass");
        }
    }
}
