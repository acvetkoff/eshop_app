package bg.softuni.eshop.user.model.binding;

import lombok.Data;

import javax.validation.constraints.Size;

import static bg.softuni.eshop.common.ExceptionMessages.COMMENT_LENGTH_MESSAGE;

@Data
public class CommentBindingModel {

    @Size(max = 300, message = COMMENT_LENGTH_MESSAGE)
    private String text;
}
