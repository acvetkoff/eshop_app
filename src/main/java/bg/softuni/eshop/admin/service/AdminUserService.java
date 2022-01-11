package bg.softuni.eshop.admin.service;

import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;

import java.util.List;

public interface AdminUserService {

    UserServiceModel findById(String id);

    List<UserServiceModel> findAllUsers();

    void deleteById(String id);

}
