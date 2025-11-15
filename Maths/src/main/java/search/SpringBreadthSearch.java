package search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entity.UserEntity;
import entity.UserRoleEntity;
import repository.UserRepository;

import java.util.*;

public class SpringBreadthSearch {
    private static final Logger log = LoggerFactory.getLogger(SpringBreadthSearch.class);
    private final UserRepository userRepository;

    public SpringBreadthSearch(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> searchFromUsername(String startUsername) {
        log.info("Starting BFS from username '{}'", startUsername);
        List<UserEntity> startList = userRepository.findByUsername(startUsername);
        if (startList.isEmpty()) return Collections.emptyList();

        Set<Long> visited = new HashSet<>();
        List<UserEntity> result = new ArrayList<>();
        Queue<UserEntity> queue = new ArrayDeque<>(startList);

        while (!queue.isEmpty()) {
            UserEntity u = queue.poll();
            if (u.getId() == null || !visited.add(u.getId())) continue;
            result.add(u);
            log.debug("BFS visited: {}", u.getUsername());

            for (UserRoleEntity ur : u.getRoles()) {
                ur.getRole().getUsers().stream()
                        .map(UserRoleEntity::getUser)
                        .filter(nei -> nei.getId() != null && !visited.contains(nei.getId()))
                        .forEach(queue::add);
            }
        }
        log.info("BFS finished, found {} users", result.size());
        return result;
    }
}
