package bg.softuni.eshop.order.model.service;

import bg.softuni.eshop.BaseServiceModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderServiceModel extends BaseServiceModel {
    private String id;
    private String address;
    private String phone;
    private String note;
    private BigDecimal totalPrice;
    private UserServiceModel user;
    private List<OrderItemServiceModel> items;
    private OrderStatus status;
    private LocalDate createdOn;
}
