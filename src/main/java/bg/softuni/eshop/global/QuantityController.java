package bg.softuni.eshop.global;

import bg.softuni.eshop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class QuantityController {

    private final OrderService orderService;

    @Autowired
    public QuantityController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute("quantity")
    public Integer attachQuantity() {
        return this.orderService.getTotalOrderItemsInOrder();
    }
}
