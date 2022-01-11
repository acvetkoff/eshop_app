package bg.softuni.eshop.user.model.entity;


import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.product.model.entity.Product;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @ManyToOne
    private UserEntity from;

    @ManyToOne
    private Product product;

    @NotEmpty
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
}
