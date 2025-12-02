package ru.ssau.tk.maths.service;

import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.entity.Point;
import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.repository.PointRepository;
import ru.ssau.tk.maths.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final AppUserRepository userRepository;
    private final FunctionRepository functionRepository;
    private final PointRepository pointRepository;

    public SearchService(AppUserRepository userRepository, FunctionRepository functionRepository, PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.functionRepository = functionRepository;
        this.pointRepository = pointRepository;
    }

    public List<Object> dfs(AppUser root) {
        log.info("[DFS] Старт для User id={}", root.getId());

        List<Object> result = new ArrayList<>();
        Deque<Object> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Object node = stack.pop();
            result.add(node);

            if (node instanceof AppUser u) {
                List<Function> funcs = functionRepository.findByUserId(u.getId());
                funcs.forEach(stack::push);
            }
            if (node instanceof Function f) {
                List<Point> pts = pointRepository.findByFunction_IdOrderByXvalueAsc(f.getId());
                pts.forEach(stack::push);
            }
        }
        return result;
    }


    public List<Object> bfs(AppUser root) {
        log.info("[BFS] Старт для User id={}", root.getId());

        List<Object> result = new ArrayList<>();
        Queue<Object> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Object node = queue.poll();
            result.add(node);

            if (node instanceof AppUser u) {
                queue.addAll(functionRepository.findByUserId(u.getId()));
            }
            if (node instanceof Function f) {
                queue.addAll(pointRepository.findByFunction_IdOrderByXvalueAsc(f.getId()));
            }
        }
        return result;
    }


    public List<Point> sortPoints(Long functionId, String field) {

        List<Point> pts = pointRepository.findByFunction_IdOrderByXvalueAsc(functionId);

        switch (field) {
            case "x" -> pts.sort(Comparator.comparing(Point::getXValue));
            case "y" -> pts.sort(Comparator.comparing(Point::getXValue));
            default -> log.warn("Неизвестное поле сортировки: {}", field);
        }

        return pts;
    }

    /** ============================
     *  Множественный поиск
     *  ============================ */
    public List<Function> multiSearch(String exprContains, String nameStarts) {
        log.info("[MultiSearch] exprContains='{}', nameStarts='{}'", exprContains, nameStarts);

        return functionRepository.findAll().stream()
                .filter(f -> f.getExpression().contains(exprContains))
                .filter(f -> f.getName().startsWith(nameStarts))
                .toList();
    }

    /** ============================
     *  Поиск по диапазону X
     *  ============================ */
    public List<Point> findPointsInRange(Long functionId, double min, double max) {
        log.info("[RangeSearch] func={} X=[{},{}]", functionId, min, max);

        return pointRepository.findByFunction_IdOrderByXvalueAsc(functionId).stream()
                .filter(p -> p.getXValue().doubleValue() >= min)
                .filter(p -> p.getXValue().doubleValue() <= max)
                .toList();
    }
}
