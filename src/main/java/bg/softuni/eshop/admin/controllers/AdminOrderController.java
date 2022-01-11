package bg.softuni.eshop.admin.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.admin.service.AdminOrderService;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.view.OrderViewModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.hibernate.criterion.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController extends BaseController {

    public static final String CONTROLLER_PREFIX = "admin/orders/";
    public static final String PAGE_PREFIX = "admin/order/";

    private final AdminOrderService orderService;

    public AdminOrderController(ModelParser modelParser, AdminOrderService adminOrderService) {
        super(modelParser);
        this.orderService = adminOrderService;
    }

    @GetMapping("/{id}")
    public String findOrderById(@PathVariable String id, Model model) {
        OrderViewModel orderViewModel = this.map(this.orderService.findById(id), OrderViewModel.class);
        model.addAttribute("order", orderViewModel);
        return this.view(PAGE_PREFIX + "order-detail");
    }

    @GetMapping
    public String findAllOrders(Model model) {
        List<OrderViewModel> orders = this.map(this.orderService.findAll(), OrderViewModel.class);
        model.addAttribute("orders", orders);
        return this.view(PAGE_PREFIX + "orders");
    }

    @GetMapping("/user/{userId}")
    public String findAllOrdersForUser(@PathVariable String userId, Model model) {
        List<OrderViewModel> orders = this.map(this.orderService.findAllByUserId(userId), OrderViewModel.class);
        model.addAttribute("orders", orders);
        return this.view(PAGE_PREFIX + "orders");
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        this.orderService.delete(id);
        return this.redirect(CONTROLLER_PREFIX);
    }

    @PutMapping("/{id}")
    public OrderViewModel changeOrderStatus(@PathVariable String id, OrderStatus orderStatus) {
        return this.map(this.orderService.changeOrderStatus(id, orderStatus), OrderViewModel.class);
    }
}
