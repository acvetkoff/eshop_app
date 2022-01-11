package bg.softuni.eshop.admin.service;

import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;

import java.util.List;

// TODO add pagination
public interface AdminOrderService {

    OrderServiceModel findById(String id);

    List<OrderServiceModel> findAll();

    List<OrderServiceModel> findAllByUserId(String user);

    void delete(String orderId);

    OrderServiceModel changeOrderStatus(String id, OrderStatus orderStatus);

    void deleteByUserId(String id);
}
