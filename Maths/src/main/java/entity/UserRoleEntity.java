package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    public UserRoleEntity() {}

    public UserRoleEntity(UserEntity user, RoleEntity role) {
        this.user = user;
        this.role = role;
    }

    public Long getId() { return id; }
    public UserEntity getUser() { return user; }
    public RoleEntity getRole() { return role; }
}

