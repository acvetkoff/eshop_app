package bg.softuni.eshop.order.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.order.model.binding.OrderItemBindingModel;
import bg.softuni.eshop.order.model.entity.OrderEntity;
import bg.softuni.eshop.order.model.service.OrderItemServiceModel;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.view.OrderItemViewModel;
import bg.softuni.eshop.order.model.view.OrderViewModel;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController extends BaseController {

    private final OrderService orderService;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;

    @Autowired
    public OrderController(ModelParser modelParser, OrderService orderService, UserService userService, CurrentlyLoggedInUser currentlyLoggedInUser) {
        super(modelParser);
        this.orderService = orderService;
        this.currentlyLoggedInUser = currentlyLoggedInUser;
    }

    @GetMapping("/details")
    public String details(Model model) {
        CustomerServiceModel customerServiceModel = this.currentlyLoggedInUser.loggedInCustomer();
        OrderServiceModel currentOrder = this.orderService.getCurrentOrder();
        OrderViewModel orderViewModel = null;
        if (currentOrder == null) {
            orderViewModel = new OrderViewModel();
            orderViewModel.setItems(new ArrayList<>()); 
        } else {
            orderViewModel = this.map(currentOrder, OrderViewModel.class);
            Integer totalOrderItemsInOrder = this.orderService.getTotalOrderItemsInOrder();
            totalOrderItemsInOrder = totalOrderItemsInOrder == null ? 0 : totalOrderItemsInOrder;
            model.addAttribute("orderItemsCount", totalOrderItemsInOrder);
        }

        orderViewModel.setAddress(customerServiceModel.getAddress());

        if (orderViewModel.getItems().isEmpty()) {
            model.addAttribute("isCartEmpty", true);
        }

        model.addAttribute("orderViewModel", orderViewModel);

        return this.view("order/cart");
    }

    @GetMapping("{id}")
    public String viewOrder(@PathVariable("id") String id, Model model) {
        OrderViewModel orderViewModel = this.map(this.orderService.getOrderById(id), OrderViewModel.class);
        model.addAttribute("order", orderViewModel);

        /*
            This is dummy page
            TODO: to be implemented
         */
        return this.view("order/order");
    }

    @PostMapping("/add")
    public String add(@ModelAttribute OrderItemBindingModel orderItemBindingModel) {
        OrderItemServiceModel orderItemServiceModel =
                this.map(orderItemBindingModel, OrderItemServiceModel.class);

        this.orderService.addOrderItem(orderItemServiceModel);

        return this.redirect(orderItemBindingModel.getCurrentPageURI());
    }

    @PostMapping("/buy")
    public String finishOrder(@ModelAttribute("orderViewModel") OrderViewModel orderViewModel, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("isOrderCompleted", true);
        this.orderService.completeOrder();
        return this.redirect("details");
    }

    @DeleteMapping("/product")
    public String removeOrderItemFromCart(@RequestParam("id") String productId){
        this.orderService.removeOrderItem(productId);

        return this.redirect("details");
    }
}
