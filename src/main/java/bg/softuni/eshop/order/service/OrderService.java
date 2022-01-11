package bg.softuni.eshop.order.service;

import bg.softuni.eshop.order.model.service.OrderItemServiceModel;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.user.model.service.UserServiceModel;

import java.util.Collection;
import java.util.List;

public interface OrderService {

    List<OrderServiceModel> getAllOrders();

    OrderServiceModel getOrderById(String id);

    OrderItemServiceModel getOrderItemById(String id);

    void addOrderItem(OrderItemServiceModel orderItemServiceModel);

    void removeOrderItem(String id);

    void edit(OrderItemServiceModel orderItemServiceModel);

    OrderServiceModel addOrderDetails();

    OrderServiceModel completeOrder();

    Collection<OrderItemServiceModel> getAllOrderItemsByOrderId(String id);

    List<OrderServiceModel> findAllByUserId(String userId);

    List<OrderItemServiceModel> findAllByUserAndStatus(UserServiceModel user, OrderStatus status);

    List<OrderItemServiceModel> findAllByStatus(OrderStatus status);

    Integer getTotalOrderItemsInOrder();

    OrderServiceModel getCurrentOrder();

    void deleteOrderItemByProduct(String productId);

    boolean exists(String id);
}
