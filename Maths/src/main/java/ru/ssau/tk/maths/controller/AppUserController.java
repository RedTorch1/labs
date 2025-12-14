package ru.ssau.tk.maths.controller;

import ru.ssau.tk.maths.dto.CreateUserRequest;
import ru.ssau.tk.maths.dto.AppUserDto;
import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOG = LoggerFactory.getLogger(AppUserController.class);

    public AppUserController(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin only: list users
    @GetMapping
    public ResponseEntity<List<AppUserDto>> listUsers() {
        LOG.info("List users requested");
        var users = userRepository.findAll().stream()
                .map(u -> new AppUserDto(u.getId(), u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // Create user (ADMIN creates any role)
    @PostMapping
    public ResponseEntity<AppUserDto> createUser(@RequestBody CreateUserRequest r) {
        LOG.info("Create user requested: username={}", r.getUsername());
        String role = r.getRole() == null ? "ROLE_USER" : r.getRole();
        AppUser u = new AppUser(r.getUsername(), passwordEncoder.encode(r.getPassword()), role);
        u = userRepository.save(u);
        AppUserDto dto = new AppUserDto(u.getId(), u.getUsername(), u.getRole());
        return ResponseEntity.created(URI.create("/api/users/" + u.getId())).body(dto);
    }

    // Register (open) — POST /api/auth/signup (but we keep here for simplicity)
    @PostMapping("/signup")
    public ResponseEntity<AppUserDto> signup(@RequestBody CreateUserRequest r) {
        LOG.info("Signup requested: username={}", r.getUsername());
        AppUser u = new AppUser(r.getUsername(), passwordEncoder.encode(r.getPassword()), "ROLE_USER");
        u = userRepository.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + u.getId()))
                .body(new AppUserDto(u.getId(), u.getUsername(), u.getRole()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable Long id, Principal principal) {
        LOG.info("Get user id={} by principal={}", id, principal == null ? "anonymous" : principal.getName());
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok(new AppUserDto(u.getId(), u.getUsername(), u.getRole())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestBody CreateUserRequest r) {
        LOG.info("Change role for id={} to {}", id, r.getRole());
        return userRepository.findById(id).map(u -> {
            u.setRole(r.getRole());
            userRepository.save(u);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        LOG.info("Delete user id={}", id);
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
