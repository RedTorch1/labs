package ru.ssau.tk.maths.controller;

import org.springframework.stereotype.Controller;
import ru.ssau.tk.maths.dto.PointDto;
import ru.ssau.tk.maths.entity.Point;
import ru.ssau.tk.maths.repository.PointRepository;
import ru.ssau.tk.maths.service.FunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/api/functions/{functionId}/points")
public class PointController {
    private static final Logger LOG = LoggerFactory.getLogger(PointController.class);

    private final FunctionService functionService;
    private final PointRepository pointRepository;

    public PointController(FunctionService functionService, PointRepository pointRepository) {
        this.functionService = functionService;
        this.pointRepository = pointRepository;
    }

    @GetMapping
    public ResponseEntity<List<PointDto>> getPoints(@PathVariable Long functionId,
                                                    @RequestParam(defaultValue = "-1.0") double xmin,
                                                    @RequestParam(defaultValue = "1.0") double xmax,
                                                    @RequestParam(defaultValue = "0.1") double step) {
        LOG.info("Get points for functionId={} xmin={} xmax={} step={}", functionId, xmin, xmax, step);
        List<Point> pts = functionService.getOrComputePoints(functionId, xmin, xmax, step);
        var dto = pts.stream().map(p -> new PointDto(p.getxvalue(), p.getyvalue())).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/recompute")
    public ResponseEntity<List<PointDto>> recompute(@PathVariable Long functionId,
                                                    @RequestParam(defaultValue = "-1.0") double xmin,
                                                    @RequestParam(defaultValue = "1.0") double xmax,
                                                    @RequestParam(defaultValue = "0.1") double step) {
        LOG.info("Recompute points for functionId={} xmin={} xmax={} step={}", functionId, xmin, xmax, step);
        List<Point> pts = functionService.recomputePoints(functionId, xmin, xmax, step);
        var dto = pts.stream().map(p -> new PointDto(p.getxvalue(), p.getyvalue())).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePoints(@PathVariable Long functionId) {
        LOG.info("Delete points for functionId={}", functionId);
        pointRepository.deleteByFunction_Id(functionId);
        return ResponseEntity.noContent().build();
    }
}
