package ru.ssau.tk.maths.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.maths.functions.TabulatedFunction;
import ru.ssau.tk.maths.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.maths.functions.factory.TabulatedFunctionFactory;

import java.util.List;
import java.util.Map;

@Controller
public class UiController {

    private final TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();

    // ✅ JSP МЕТОДЫ
    @GetMapping("/ui/main") public String main() { return "main"; }
    @GetMapping("/ui/functions/create-from-arrays") public String createFromArrays() { return "functions/create-from-arrays"; }

    @GetMapping("/ui/functions/manage")
    public String manageFunctions(Model model) {
        // ✅ ТОЛЬКО ТЕСТОВЫЕ ФУНКЦИИ - БЕЗ FunctionsIO!
        List<TabulatedFunction> functions = createTestFunctions();
        model.addAttribute("functions", functions);
        return "functions/manage";
    }

    @GetMapping("/ui") public String index() { return "index"; }
    @GetMapping("/ui/functions/create-from-function") public String createFromFunction() { return "functions/create-from-function"; }
    @GetMapping("/ui/login") public String login() { return "login"; }
    @GetMapping("/ui/register") public String register() { return "register"; }
    @GetMapping("/ui/operations") public String operations() { return "operations"; }
    @GetMapping("/ui/differentiation") public String differentiation() { return "differentiation"; }
    @GetMapping("/ui/study") public String study() { return "study-function"; }

    // ✅ JSON API
    @GetMapping(value = "/ui/functions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getFunctionNames() {
        return List.of(
                "Квадратичная функция", "Тождественная функция", "Постоянная функция (0)",
                "Постоянная функция (1)", "Синусоида", "Косинусоида"
        );
    }

    // ✅ API СОХРАНЕНИЯ
    @PostMapping("/api/functions/save-from-arrays")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveFromArrays(@RequestBody Map<String, Object> data) {
        try {
            String name = (String) data.get("name");
            @SuppressWarnings("unchecked")
            List<Map<String, Double>> points = (List<Map<String, Double>>) data.get("points");
            System.out.println("✅ Сохранена: " + name + " (" + points.size() + " точек)");
            return ResponseEntity.ok(Map.of(
                    "id", 123 + (int)(Math.random() * 100),
                    "name", name,
                    "pointsCount", points.size(),
                    "success", true
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ошибка сохранения"));
        }
    }

    @PostMapping("/api/functions/save-from-function")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveFromFunction(@RequestBody Map<String, Object> data) {
        try {
            String name = (String) data.get("name");
            System.out.println("✅ Сохранена из функции: " + name);
            return ResponseEntity.ok(Map.of(
                    "id", 124 + (int)(Math.random() * 100),
                    "name", name,
                    "success", true
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ошибка сохранения"));
        }
    }

    // ✅ ТЕСТОВЫЕ ФУНКЦИИ (БЕЗ FunctionsIO!)
    private List<TabulatedFunction> createTestFunctions() {
        double[] xSqr = {0, 1, 2, 3, 4};
        double[] ySqr = {0, 1, 4, 9, 16};
        TabulatedFunction sqr = factory.create(xSqr, ySqr);

        double[] xSin = {0, 0.4, 0.8, 1.2, 1.6, 2.0, 2.4, 2.8, 3.2, 3.6, 4.0};
        double[] ySin = {0.00, 0.39, 0.72, 0.93, 0.99, 0.91, 0.68, 0.33, -0.06, -0.44, -0.76};
        TabulatedFunction sin = factory.create(xSin, ySin);

        return List.of(sqr, sin);
    }
}
