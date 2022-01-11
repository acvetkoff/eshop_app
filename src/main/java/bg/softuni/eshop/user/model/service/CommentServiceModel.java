package bg.softuni.eshop.user.model.service;


import bg.softuni.eshop.BaseServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.user.model.entity.UserEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static bg.softuni.eshop.common.ExceptionMessages.COMMENT_LENGTH_MESSAGE;

@Data
public class CommentServiceModel extends BaseServiceModel {

    @NotNull
    private UserServiceModel from;

    @NotNull
    private ProductServiceModel product;

    @Size(max = 300, message = COMMENT_LENGTH_MESSAGE)
    private String text;

    private LocalDate createdOn;
}
