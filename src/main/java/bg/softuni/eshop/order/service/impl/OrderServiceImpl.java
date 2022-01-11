package bg.softuni.eshop.order.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.order.dao.OrderItemRepository;
import bg.softuni.eshop.order.dao.OrderRepository;
import bg.softuni.eshop.order.model.entity.OrderEntity;
import bg.softuni.eshop.order.model.entity.OrderItemEntity;
import bg.softuni.eshop.order.model.entity.PaymentEntity;
import bg.softuni.eshop.order.model.service.OrderItemServiceModel;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.order.model.еnums.PaymentStatus;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.user.model.entity.UserEntity;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static bg.softuni.eshop.common.ExceptionMessages.*;
import static bg.softuni.eshop.order.model.еnums.OrderStatus.*;
import static java.lang.Boolean.FALSE;
import static java.math.BigDecimal.ZERO;

@Service
@SessionScope
public class OrderServiceImpl extends BaseService implements OrderService {

	private final ProductService productService;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final CurrentlyLoggedInUser loggedInUser;
	private final UserService userService;

	private OrderEntity orderEntity;

	@Autowired
	public OrderServiceImpl(ProductService productService, ModelParser modelParser, OrderRepository orderRepository,
							CurrentlyLoggedInUser loggedInUser, OrderItemRepository orderItemRepository, UserService userService) {
		super(modelParser);
		this.productService = productService;
		this.orderRepository = orderRepository;
		this.loggedInUser = loggedInUser;
		this.orderItemRepository = orderItemRepository;
		this.userService = userService;
	}

	@Override
	public OrderItemServiceModel getOrderItemById(String id) {
		return this.orderItemRepository.findById(id)
				.map(orderItemEntity -> map(orderItemEntity, OrderItemServiceModel.class))
				.orElseThrow(() -> new IllegalArgumentException(
						String.format(NON_EXISTING_ORDER_ITEM, id)));
	}

	@Override
	public void addOrderItem(OrderItemServiceModel orderItemServiceModel) {
		this.createNewOrderIfNeeded();

		ProductServiceModel product = this.productService.getById(orderItemServiceModel.getProductId());

		Optional<OrderItemEntity> orderItemEntityOpt = this
				.getOrderItemEntityByProductId(orderItemServiceModel.getProductId());

		if (orderItemEntityOpt.isPresent()) {
			int currentQuantity = orderItemEntityOpt.get().getQuantity();
			this.updateExistingOrderItem(orderItemEntityOpt, currentQuantity + 1);
		} else {
			orderItemEntityOpt = this.createNewOrderItem(orderItemServiceModel, product);

			this.orderEntity.addOrderItem(orderItemEntityOpt.get());
			// state is kept in the session, no need to save it ...
		}

		this.recalculateOrderItemPrice(product, orderItemEntityOpt.get());
		this.orderEntity.setTotalPrice(this.calculateTotalPrice());
	}

	private void updateCurrentOrderPrice(OrderItemEntity orderItemEntity) {
		BigDecimal currentTotalPrice = this.orderEntity.getTotalPrice();
		this.orderEntity
				.setTotalPrice(currentTotalPrice.add(orderItemEntity.getTotalPrice()));
	}

	@Override
	public void removeOrderItem(String productId) {
		this.validateOrderExists();

		ProductServiceModel productServiceModel = this.productService.getById(productId);

		Optional<OrderItemEntity> orderItemEntityOptional = this.orderEntity.getItems()
				.stream()
				.filter(orderItemEntity ->
						orderItemEntity.getProduct().getId().equals(productServiceModel.getId()))
				.findFirst();

		orderItemEntityOptional
				.ifPresent(orderItemEntity -> this.orderEntity.getItems().remove(orderItemEntity));
		this.orderEntity.setTotalPrice(this.calculateTotalPrice());
	}

	@Override
	public void edit(OrderItemServiceModel orderItemServiceModel) {
		this.validateOrderExists();
		this.validateId(orderItemServiceModel.getId(), INVALID_ORDER_ITEM_ID_MESSAGE);
		this.validateId(orderItemServiceModel.getProductId(), INVALID_PRODUCT_ID_MESSAGE);

		ProductServiceModel product = this.productService.getById(orderItemServiceModel.getProductId());
		Optional<OrderItemEntity> orderItemEntityOpt = this.getOrderItemEntityById(orderItemServiceModel.getId());
		if (orderItemEntityOpt.isEmpty()) {
			throw new IllegalArgumentException(
					"Nothing to update " + orderItemServiceModel.getId() + " in order " + orderEntity.getId());
		}

		this.updateExistingOrderItem(orderItemEntityOpt, orderItemServiceModel.getQuantity());
		this.recalculateOrderItemPrice(product, orderItemEntityOpt.get());
	}

	@Override
	@Transactional
	public OrderServiceModel completeOrder() {
		this.validateOrderExists();
		this.addOrderDetails();
		this.orderEntity.setStatus(COMPLETED);
		// TODO: upcoming payment logic
		List<PaymentEntity> payments = new ArrayList<>() {{ add(new PaymentEntity(PaymentStatus.PENDING)); }};
		this.orderEntity.setPayment(payments);
		OrderServiceModel savedOrder = this.map(this.orderRepository.save(this.orderEntity), OrderServiceModel.class);
		this.orderEntity = null;
		return savedOrder;
	}

	@Override
	public Collection<OrderItemServiceModel> getAllOrderItemsByOrderId(String id) {
		Optional<OrderEntity> orderOpt = this.orderRepository.findById(id);

		// TODO: create custom exception
		if (orderOpt.isEmpty()) {
			throw new IllegalArgumentException();
		}

		return orderOpt.get().getItems().stream().map(order -> map(order, OrderItemServiceModel.class))
				.collect(Collectors.toList());
	}

	@Override
	public OrderServiceModel addOrderDetails() {
		CustomerServiceModel loggedInCustomer = this.loggedInUser.loggedInCustomer();
		this.orderEntity.setAddress(loggedInCustomer.getAddress());
		this.orderEntity.setPhone(loggedInCustomer.getPhone());

		// TODO: add note from the cart page
		this.orderEntity.setNote("TO be added");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		UserEntity user = this.map(this.userService.getByUsername(userDetails.getUsername()), UserEntity.class);
		this.orderEntity.setUser(user);

		this.orderEntity.setStatus(PENDING);
		//this.orderEntity = this.orderRepository.save(this.orderEntity);
		OrderServiceModel orderServiceModel = this.map(this.orderEntity, OrderServiceModel.class);
		return orderServiceModel;
	}

	private void validateId(String id, String message) {
		if (id == null || id.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

	private BigDecimal calculateTotalPrice() {
		return this.orderEntity.getItems()
				.stream()
				.map(OrderItemEntity::getTotalPrice)
				.reduce(ZERO, BigDecimal::add);
	}

	private Optional<OrderItemEntity> createNewOrderItem(OrderItemServiceModel orderItemServiceModel,
			ProductServiceModel productServiceModel) {
		OrderItemEntity newOrderItemEntity = new OrderItemEntity();
		Product product = this.map(productServiceModel, Product.class);
		this.setProductInStock(product);

		// TODO: throw exception if product is not in stock
//        if (product.isInStock()) {
//            newOrderItemEntity.setQuantity(orderItemServiceModel.getQuantity());
//            newOrderItemEntity.setProduct(product);
//            this.reduceProductQuantity(product);
//        }

		newOrderItemEntity.setQuantity(orderItemServiceModel.getQuantity());
		newOrderItemEntity.setProduct(product);
//		newOrderItemEntity
//				.setTotalPrice(new BigDecimal(orderItemServiceModel.getQuantity()).multiply(product.getPrice()));
		return Optional.of(newOrderItemEntity);
	}

	private void setProductInStock(Product product) {
		if (product.getQuantity() <= 0) {
			product.setInStock(FALSE);
		}
	}

	private void reduceProductQuantity(Product product) {
		product.setQuantity(product.getQuantity() - 1);
	}

	private void recalculateOrderItemPrice(ProductServiceModel product, OrderItemEntity newOrderItemEntity) {
		BigDecimal productPrice = product.getPrice();
		Integer quantity = newOrderItemEntity.getQuantity();
		BigDecimal quantityAsBigDecimal = new BigDecimal(String.valueOf(quantity), MathContext.DECIMAL32);
		newOrderItemEntity.setTotalPrice(quantityAsBigDecimal.multiply(productPrice));
	}


	private void updateExistingOrderItem(Optional<OrderItemEntity> orderItemEntityOpt, Integer newQuantity) {
		orderItemEntityOpt.get().setQuantity(newQuantity);
	}

	private void createNewOrderIfNeeded() {
		if (this.orderEntity == null) {
			this.orderEntity = new OrderEntity();
			this.orderEntity.setStatus(NEW);
			this.orderEntity.setItems(new ArrayList<>());
			this.orderEntity.setTotalPrice(ZERO);
		}
	}

	private void validateOrderExists() {
		if (this.orderEntity == null) {
			throw new IllegalArgumentException(NO_ACTIVE_ORDER_MESSAGE);
		}
	}

	private Optional<OrderItemEntity> getOrderItemEntityByProductId(String productId) {
		return this.orderEntity
				.getItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst();
	}

	private Optional<OrderItemEntity> getOrderItemEntityById(String id) {
		return this.orderEntity
				.getItems()
				.stream()
				.filter(item -> ((item != null) && item.getId().equals(id)))
				.findFirst();
	}

	@Override
	public OrderServiceModel getOrderById(String id) {
		Optional<OrderEntity> optionalOrder = this.orderRepository.findById(id);
		return this.map(optionalOrder.get(), OrderServiceModel.class);
	}

	@Override
	public List<OrderServiceModel> getAllOrders() {
		return orderRepository
				.findAll()
				.stream()
				.map(order -> this.map(order, OrderServiceModel.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderServiceModel> findAllByUserId(String userId) {
		return this.map(orderRepository.findAllByUserId(userId), OrderServiceModel.class);
	}

	@Override
	public List<OrderItemServiceModel> findAllByUserAndStatus(UserServiceModel user, OrderStatus status) {
		return this.map(orderRepository.findAllByUserIdAndStatus(user.getId(), status), OrderItemServiceModel.class);
	}

	@Override
	public List<OrderItemServiceModel> findAllByStatus(OrderStatus status) {
		return this.map(orderRepository.findAllByStatus(status), OrderItemServiceModel.class);
	}

	@Override
	public Integer getTotalOrderItemsInOrder() {
		return this.orderEntity == null? 0 : this.orderEntity.getItems().size();
	}

	@Override
	public OrderServiceModel getCurrentOrder() {
		if (this.orderEntity == null) {
			return null;
		}
		return this.map(this.orderEntity, OrderServiceModel.class);
	}

	@Override
	@Transactional
	public void deleteOrderItemByProduct(String productId) {
		List<OrderEntity> orders = orderRepository.findAllByItemsProductId(productId);

		for (OrderEntity orderEntity : orders) {
            orderEntity.getItems().removeIf(itemEntity -> productId.equals(itemEntity.getProduct().getId()));
			orderRepository.saveAndFlush(orderEntity);
		}
	}

	@Override
	public boolean exists(String id) {
		if (id == null) {
			return false;
		}

		return this.orderRepository.existsById(id);
	}
}
