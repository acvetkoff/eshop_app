package bg.softuni.eshop.global;

import bg.softuni.eshop.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleProductNotFoundException() {
        return "error/404";
    }
}
