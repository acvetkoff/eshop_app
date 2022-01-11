package bg.softuni.eshop.user.validation;

import bg.softuni.eshop.user.validation.annotations.MatchFields;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchFieldsValidator implements ConstraintValidator<MatchFields, Object> {

    private String firstField;
    private String secondField;
    private String message;

    @Override
    public void initialize(MatchFields constraintAnnotation) {
        this.firstField = constraintAnnotation.first();
        this.secondField = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

        Object firstFieldValue = beanWrapper.getPropertyValue(this.firstField);
        Object secondFieldValue = beanWrapper.getPropertyValue(this.secondField);
        boolean valid;

        if (firstFieldValue == null) {
            valid = secondFieldValue == null;
        } else {
            valid = firstFieldValue.equals(secondFieldValue);
        }

        return valid;
    }
}
