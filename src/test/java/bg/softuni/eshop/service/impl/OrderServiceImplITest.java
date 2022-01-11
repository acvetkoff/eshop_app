package bg.softuni.eshop.service.impl;

import bg.softuni.eshop.order.model.service.OrderItemServiceModel;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for completing order process
 * 
 */
@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceImplITest extends BaseIntegrationTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Test
	public void testCompleteOrder() {
		UserServiceModel userServiceModel = this.registerAndLogin("testCompleteOrder", "testCompleteOrder");

		userServiceModel.getCustomer().setAddress("address");
		userServiceModel.getCustomer().setPhone("phone");

		userService.updateProfile(userServiceModel);

		List<ProductServiceModel> allProducts = productService.getAll();

		List<OrderItemServiceModel> orderItems = allProducts.stream()
				.map(product -> new OrderItemServiceModel(null, null, product.getId(), 1, null))
				.collect(Collectors.toList());

		orderItems.forEach(orderService::addOrderItem);

		OrderServiceModel pendingOrder = orderService.addOrderDetails();
		assertEquals(OrderStatus.PENDING, pendingOrder.getStatus());

		OrderServiceModel completedOrder = orderService.completeOrder();
		assertEquals(OrderStatus.COMPLETED, completedOrder.getStatus());

		OrderServiceModel existingOrder = orderService.getOrderById(completedOrder.getId());

		assertNotNull(existingOrder);
		assertTrue(existingOrder.getTotalPrice().compareTo(BigDecimal.valueOf(0)) > 0);
		assertEquals(OrderStatus.COMPLETED, existingOrder.getStatus());
		assertFalse(orderService.getAllOrders().isEmpty());

		assertEquals(1, orderService.findAllByStatus(OrderStatus.COMPLETED).size());
		assertEquals(1, orderService.findAllByUserAndStatus(userServiceModel, OrderStatus.COMPLETED).size());
		assertEquals(1, orderService.findAllByStatus(OrderStatus.COMPLETED).size());
	}
}
