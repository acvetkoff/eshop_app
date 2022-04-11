package bg.softuni.eshop.user.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.order.dao.OrderRepository;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.view.OrderViewModel;
import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.user.dao.CommentRepository;
import bg.softuni.eshop.user.dao.CustomerRepository;
import bg.softuni.eshop.user.dao.UserRepository;
import bg.softuni.eshop.user.model.entity.CustomerEntity;
import bg.softuni.eshop.user.model.entity.UserEntity;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static bg.softuni.eshop.common.ExceptionMessages.USER_NOT_FOUND;

@Service
@SessionScope
public class CurrentlyLoggedInServiceImpl extends BaseService implements CurrentlyLoggedInUser {

	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final CommentRepository commentRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public CurrentlyLoggedInServiceImpl(CustomerRepository customerRepository, UserRepository userRepository,
										ModelParser modelParser, CommentRepository commentRepository, OrderRepository orderRepository) {
		super(modelParser);
		this.customerRepository = customerRepository;
		this.userRepository = userRepository;
		this.commentRepository = commentRepository;
		this.orderRepository = orderRepository;
	}

	public CustomerServiceModel loggedInCustomer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		UserEntity user = this.findUserByUsername(userDetails.getUsername());
		Optional<CustomerEntity> customerOpt = this.customerRepository.findByUserUsername(user.getUsername());

		return this.map(customerOpt.get(), CustomerServiceModel.class);
	}

	@Override
	public List<OrderServiceModel> getCustomerOrders() {
		return null;
	}

	@Override
	public List<CommentServiceModel> getCustomerComments(String productId, String userId) {
		return this.map(this.commentRepository.getAllByProductIdAndFromId(productId ,userId), CommentServiceModel.class);
	}

	@Override
	public List<CommentServiceModel> getCustomerComments(String userId) {
		return this.map(this.commentRepository.getAllByFromId(userId), CommentServiceModel.class);
	}

	private UserEntity findUserByUsername(String username) {
		return this.userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
	}
}
