package bg.softuni.eshop.user.model.service;

import bg.softuni.eshop.BaseServiceModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static bg.softuni.eshop.common.ExceptionMessages.*;

@Data
public class UserServiceModel extends BaseServiceModel {
    @Size(min = 2, max = 20, message = INVALID_USERNAME_MESSAGE)
    private String username;

    @Size(min = 2, max = 20, message = INVALID_PASSWORD_MESSAGE)
    private String password;

    @NotNull
    private String email;

    @NotNull
    private CustomerServiceModel customer;

    private String[] roles;
}
