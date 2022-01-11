package bg.softuni.eshop.user.model.entity;

import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.user.model.enums.UserRole;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "user_roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEntity extends BaseEntity {

    @Expose
    @Enumerated(STRING)
    protected UserRole userRole;
}
