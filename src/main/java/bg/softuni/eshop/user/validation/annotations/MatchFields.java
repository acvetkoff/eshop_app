package bg.softuni.eshop.user.validation.annotations;

import bg.softuni.eshop.user.validation.MatchFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MatchFieldsValidator.class)
public @interface MatchFields {

    String message() default "Fields do not match!";

    String first();

    String second();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE})
    @Retention(RUNTIME)
    @interface List {
        MatchFields[] value();
    }
}