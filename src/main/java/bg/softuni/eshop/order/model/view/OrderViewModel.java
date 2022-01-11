package bg.softuni.eshop.order.model.view;

import bg.softuni.eshop.order.model.еnums.OrderStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderViewModel  {
    private String id;
    private String address;
    private String phone;
    private String note;
    private BigDecimal totalPrice;
    private List<OrderItemViewModel> items;
    private OrderStatus status;
    private boolean isActive;
    private LocalDate createdOn;
}
