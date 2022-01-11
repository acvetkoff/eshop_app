package bg.softuni.eshop.order.model.entity;

import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.product.model.entity.Product;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItemEntity extends BaseEntity {

    @NotNull
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Product product;

    @Column(name = "quantity", nullable = false)
    @NotNull
    private Integer quantity;

    @Column(nullable = false)
    @NotNull
    private BigDecimal totalPrice;
}
