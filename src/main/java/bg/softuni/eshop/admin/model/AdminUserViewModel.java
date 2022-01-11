package bg.softuni.eshop.admin.model;

import bg.softuni.eshop.BaseServiceModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static bg.softuni.eshop.common.ExceptionMessages.INVALID_PASSWORD_MESSAGE;
import static bg.softuni.eshop.common.ExceptionMessages.INVALID_USERNAME_MESSAGE;

@Data
public class AdminUserViewModel extends BaseServiceModel {
    @Size(min = 2, max = 20, message = INVALID_USERNAME_MESSAGE)
    private String username;

    @Size(min = 2, max = 20, message = INVALID_PASSWORD_MESSAGE)
    private String password;

    @NotNull
    private String email;

    @NotNull
    private AdminCustomerViewModel customer;

    private String[] roles;
}
