package ru.ssau.tk.maths.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public CustomUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь '" + username + "' не найден"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .roles(user.getRole().replace("ROLE_", ""))  // ROLE_ADMIN → ADMIN
                .build();
    }
}
