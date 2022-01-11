package bg.softuni.eshop.user.model.entity;

import bg.softuni.eshop.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = EAGER)
    private Set<UserRoleEntity> roles;

    @OneToOne(cascade = CascadeType.ALL)
    private CustomerEntity customer;

    public void addRole(UserRoleEntity userRole) {
        this.roles.add(userRole);
    }
}
