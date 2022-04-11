package bg.softuni.eshop.exceptions;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ValidationException extends BindException {
    public ValidationException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
