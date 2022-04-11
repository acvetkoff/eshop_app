package bg.softuni.eshop.user.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.exceptions.UserAlreadyPersistedException;
import bg.softuni.eshop.exceptions.ValidationException;
import bg.softuni.eshop.user.model.binding.UserRegisterBindingModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static bg.softuni.eshop.common.GlobalConstants.INVALID_PASSWORD;
import static bg.softuni.eshop.common.GlobalConstants.INVALID_USERNAME_MESSAGE;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(ModelParser modelParser, UserService userService) {
        super(modelParser);
        this.userService = userService;
    }

    @ModelAttribute("userRegisterBindingModel")
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/register")
    public String register() {
        return this.view("user/register");
    }

    @PostMapping("/register")
    public String registerPost(@Valid UserRegisterBindingModel userRegisterBindingModel,
                               BindingResult bindingResult) throws BindException {
        this.userService.ensureCredentials(bindingResult);
        UserServiceModel userServiceModel = this.modelParser.convert(userRegisterBindingModel, UserServiceModel.class);
        this.userService.registerAndLogin(userServiceModel);

        return this.redirect("/");
    }

    @GetMapping("/login")
    public String login() {
        return this.view("user/login");
    }

    @PostMapping("/login-error")
    public String loginError(@ModelAttribute(SPRING_SECURITY_FORM_USERNAME_KEY)
                                     String username,
                             @ModelAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY)
                                     String password,
                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        Collection<String> errorMessages = new ArrayList<>();
        if (this.userService.exists(username)) {
            if (!this.userService.isPasswordValid(username, password)) {
                errorMessages.add(INVALID_PASSWORD);
            }
        } else {
            String errorMessage = String.format(INVALID_USERNAME_MESSAGE, username);
            errorMessages.add(errorMessage);
        }

        redirectAttributes.addFlashAttribute("errors", errorMessages);

        return this.redirect("login");
    }

    @ExceptionHandler(UserAlreadyPersistedException.class)
    public String handleUsernameExistsException(UserAlreadyPersistedException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userExists", true);
        redirectAttributes.addFlashAttribute("existsMessage", exception.getMessage());
        return this.redirect("register");
    }

    @ExceptionHandler(ValidationException.class)
    public String handleInvalidUserCredentialException(ValidationException exception, RedirectAttributes  redirectAttributes) {
        redirectAttributes.addFlashAttribute("invalidCredentials", true);

        exception.getFieldErrors()
                        .forEach(err -> redirectAttributes.addFlashAttribute(
                                err.getField() + "ErrorMessage", err.getDefaultMessage()));

        // Needed for custom validation annotation that targets classes, not fields
        exception.getAllErrors()
                .forEach(err -> redirectAttributes.addFlashAttribute(
                        "confirmPassErrorMessage" , err.getDefaultMessage()));

        return this.redirect("register");
    }
}
