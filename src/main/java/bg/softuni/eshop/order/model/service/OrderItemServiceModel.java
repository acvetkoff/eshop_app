package bg.softuni.eshop.order.model.service;

import bg.softuni.eshop.product.model.service.ProductServiceModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemServiceModel {

    private String id;

    private ProductServiceModel product;

    @NotNull
    private String productId;

    @NotNull
    private Integer quantity;

    private BigDecimal totalPrice;
}
