package bg.softuni.eshop.order.model.binding;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderItemBindingModel {

    @NotNull
    private String productId;
    @NotNull
    private Integer quantity;

    private String currentPageURI;
}
