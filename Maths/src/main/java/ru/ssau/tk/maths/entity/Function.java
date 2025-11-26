package ru.ssau.tk.maths.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "function")
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // отображаемое имя функции
    private String name;

    // выражение, например: sin(x) + 0.5 * x^2
    @Column(nullable = false, length = 1000)
    private String expression;

    // владелец (пользователь)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    public Function() {}

    public Function(String name, String expression, AppUser user) {
        this.name = name;
        this.expression = expression;
        this.user = user;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
