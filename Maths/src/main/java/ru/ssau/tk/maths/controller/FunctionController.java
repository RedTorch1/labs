package ru.ssau.tk.maths.controller;

import ru.ssau.tk.maths.dto.FunctionDto;
import ru.ssau.tk.maths.entity.Function;
import ru.ssau.tk.maths.entity.AppUser;
import ru.ssau.tk.maths.repository.FunctionRepository;
import ru.ssau.tk.maths.repository.AppUserRepository;
import ru.ssau.tk.maths.service.FunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/functions")
public class FunctionController {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionController.class);
    private final FunctionRepository functionRepository;
    private final AppUserRepository userRepository;
    private final FunctionService functionService;

    public FunctionController(FunctionRepository functionRepository,
                              AppUserRepository userRepository,
                              FunctionService functionService) {
        this.functionRepository = functionRepository;
        this.userRepository = userRepository;
        this.functionService = functionService;
    }

    @GetMapping
    public ResponseEntity<List<FunctionDto>> list(@RequestParam Long userId, Principal principal) {
        LOG.info("List functions by userId={} requested by {}", userId, principal == null ? "anon" : principal.getName());
        List<Function> list = functionRepository.findByUserId(userId);
        var dtos = list.stream().map(f -> {
            FunctionDto d = new FunctionDto();
            d.setId(f.getId()); d.setName(f.getName()); d.setExpression(f.getExpression()); d.setUserId(f.getUser().getId());
            return d;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<FunctionDto> create(@RequestBody FunctionDto request, Principal principal) {
        LOG.info("Create function requested by {}: name={}", principal == null ? "anon" : principal.getName(), request.getName());
        AppUser owner = userRepository.findById(request.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Function f = new Function(request.getName(), request.getExpression(), owner);
        f = functionRepository.save(f);
        request.setId(f.getId());
        return ResponseEntity.ok(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunctionDto> get(@PathVariable Long id) {
        LOG.info("Get function id={}", id);
        return functionRepository.findById(id).map(f -> {
            FunctionDto d = new FunctionDto(); d.setId(f.getId()); d.setName(f.getName()); d.setExpression(f.getExpression()); d.setUserId(f.getUser().getId());
            return ResponseEntity.ok(d);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FunctionDto req) {
        LOG.info("Update function id={}", id);
        return functionRepository.findById(id).map(f -> {
            f.setName(req.getName()); f.setExpression(req.getExpression());
            functionRepository.save(f);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        LOG.info("Delete function id={}", id);
        if (!functionRepository.existsById(id)) return ResponseEntity.notFound().build();
        functionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
