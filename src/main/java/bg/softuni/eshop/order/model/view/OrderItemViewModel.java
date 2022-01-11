package bg.softuni.eshop.order.model.view;

import bg.softuni.eshop.product.model.view.ProductViewModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemViewModel {
    private String id;
    private ProductViewModel product;
    private Integer quantity;
    private BigDecimal totalPrice;
}

