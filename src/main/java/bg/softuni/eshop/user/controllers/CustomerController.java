package bg.softuni.eshop.user.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.view.OrderItemViewModel;
import bg.softuni.eshop.order.model.view.OrderViewModel;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.view.CustomerProfileViewModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/customers")
public class CustomerController extends BaseController {

    private final CurrentlyLoggedInUser currentlyLoggedInUser;
    private final ProductService productService;
    private final CommentService commentService;
    private final OrderService orderService;

    @Autowired
    public CustomerController(ModelParser modelParser, CurrentlyLoggedInUser currentlyLoggedInUser, ProductService productService, CommentService commentService, OrderService orderService) {
        super(modelParser);
        this.currentlyLoggedInUser = currentlyLoggedInUser;
        this.productService = productService;
        this.commentService = commentService;
        this.orderService = orderService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        CustomerProfileViewModel customerProfileViewModel =
                this.map(this.currentlyLoggedInUser.loggedInCustomer(), CustomerProfileViewModel.class);
        model.addAttribute("customer", customerProfileViewModel);

        return this.view("customer-profile");
    }

    @GetMapping("/profile/comments")
    public String viewCustomerComments(Model model) {
        CustomerProfileViewModel customerProfileViewModel =
                this.map(this.currentlyLoggedInUser.loggedInCustomer(), CustomerProfileViewModel.class);

        List<CommentServiceModel> serviceModels =
                this.currentlyLoggedInUser.getCustomerComments(customerProfileViewModel.getUserId());

        List<CommentViewModel> customerComments = this.map(serviceModels, CommentViewModel.class);
        customerComments.forEach(comment ->
                comment.setDaysPassed(this.commentService.getDaysPassed(comment.getCreatedOn())));

        model.addAttribute("comments", customerComments);

        return this.view("my-reviews");
    }

    @GetMapping("/profile/orders")
    public String viewCustomerOrders(Model model) {
        CustomerProfileViewModel customerProfileViewModel =
                this.map(this.currentlyLoggedInUser.loggedInCustomer(), CustomerProfileViewModel.class);

        List<OrderServiceModel> orderServiceModels =
                this.orderService.findAllByUserId(customerProfileViewModel.getUserId());

        List<OrderViewModel> orders = this.map(orderServiceModels, OrderViewModel.class);

        if (orders.isEmpty()) {
            model.addAttribute("noOrders", true);
        }

        List<String> genres = orders.stream()
                .flatMap(orderViewModel -> orderViewModel.getItems().stream())
                .flatMap(orderItemViewModel -> Stream.of(orderItemViewModel.getProduct().getGenres()))
                .collect(Collectors.toList());

        List<ProductViewModel> suggested =
                this.map(this.productService.getByGenresKeyIn(genres.toArray(String[]::new)), ProductViewModel.class);

        model.addAttribute("orders", orders);
        model.addAttribute("suggested", suggested);

        return this.view("my-orders");
    }
}
