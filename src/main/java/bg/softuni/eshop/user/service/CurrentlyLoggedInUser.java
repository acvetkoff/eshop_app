package bg.softuni.eshop.user.service;


import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.view.OrderViewModel;
import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;

import java.util.List;

public interface CurrentlyLoggedInUser {

	CustomerServiceModel loggedInCustomer();

	List<OrderServiceModel> getCustomerOrders();

	List<CommentServiceModel> getCustomerComments(String productId, String userId);

	List<CommentServiceModel> getCustomerComments(String userId);
}
