package bg.softuni.eshop.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class UserAlreadyPersistedException extends RuntimeException {
    public UserAlreadyPersistedException(String message) {
        super(message);
    }
}
