package bg.softuni.eshop.user.model.binding;

import bg.softuni.eshop.user.validation.annotations.MatchFields;
import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static bg.softuni.eshop.common.ExceptionMessages.*;

@Data
@MatchFields.List(
        @MatchFields(
        first = "password",
        second = "confirmPassword",
        message = PASSWORDS_MISMATCH_MESSAGE
))
public class UserRegisterBindingModel {

    @Expose
    @Size(min = 2, max = 20, message = INVALID_USERNAME_MESSAGE)
    private String username;

    @Expose
    @Size(min = 2, max = 20, message = INVALID_PASSWORD_MESSAGE)
    private String password;

    @Expose
    private String confirmPassword;

    @Expose
    @NotNull
    @Email(message = INVALID_EMAIL_MESSAGE)
    private String email;

    @Expose
    private String confirmEmail;

    @Expose
    private CustomerBindingModel customer;

    @Expose
    private String[] roles;
}
