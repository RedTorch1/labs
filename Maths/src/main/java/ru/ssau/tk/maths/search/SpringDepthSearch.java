package ru.ssau.tk.maths.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ssau.tk.maths.entity.UserEntity;
import ru.ssau.tk.maths.entity.UserRoleEntity;
import ru.ssau.tk.maths.repository.UserRepository;

import java.util.*;

public class SpringDepthSearch {
    private static final Logger log = LoggerFactory.getLogger(SpringDepthSearch.class);
    private final UserRepository userRepository;

    public SpringDepthSearch(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // depth search across users connected via roles starting from a username
    public List<UserEntity> searchFromUsername(String startUsername) {
        log.info("Starting DFS from username '{}'", startUsername);
        List<UserEntity> startList = userRepository.findByUsername(startUsername);
        if (startList.isEmpty()) return Collections.emptyList();

        Set<Long> visited = new HashSet<>();
        List<UserEntity> result = new ArrayList<>();
        Deque<UserEntity> stack = new ArrayDeque<>(startList);

        while (!stack.isEmpty()) {
            UserEntity u = stack.pop();
            if (u.getId() == null || !visited.add(u.getId())) continue;
            result.add(u);
            log.debug("DFS visited: {}", u.getUsername());

            // neighbours: users who share any role with u
            for (UserRoleEntity ur : u.getRoles()) {
                ur.getRole().getUsers().stream()
                        .map(UserRoleEntity::getUser)
                        .filter(nei -> nei.getId() != null && !visited.contains(nei.getId()))
                        .forEach(stack::push);
            }
        }
        log.info("DFS finished, found {} users", result.size());
        return result;
    }
}
