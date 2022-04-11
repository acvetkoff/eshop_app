package bg.softuni.eshop.user.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.user.model.binding.CommentBindingModel;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products/{productId}/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;
    private final ProductService productService;
    private final CurrentlyLoggedInUser loggedInUser;

    @Autowired
    public CommentController(ModelParser modelParser, CommentService commentService, ProductService productService, CurrentlyLoggedInUser loggedInUser) {
        super(modelParser);
        this.commentService = commentService;
        this.productService = productService;
        this.loggedInUser = loggedInUser;
    }

    @GetMapping
    public String getComments(@PathVariable String productId, Model model) {
        List<CommentViewModel> comments = this.map(this.commentService.getComments(), CommentViewModel.class);

        ProductServiceModel productServiceModel = this.productService.getById(productId);
        CustomerServiceModel customerServiceModel = this.loggedInUser.loggedInCustomer();

        model.addAttribute("comments", comments);
        model.addAttribute("from", customerServiceModel.getUser().getUsername());
        model.addAttribute("product", productServiceModel);

        return this.view("user/comment");
    }

    @PostMapping
    public String addComment(@ModelAttribute CommentBindingModel commentBindingModel,
                             @PathVariable String productId) {
        ProductServiceModel productServiceModel = this.productService.getById(productId);
        CommentServiceModel commentServiceModel = this.map(commentBindingModel, CommentServiceModel.class);
        commentServiceModel.setProduct(productServiceModel);
        CustomerServiceModel customerServiceModel = this.loggedInUser.loggedInCustomer();
        commentServiceModel.setFrom(customerServiceModel.getUser());
        this.commentService.addComment(commentServiceModel);

        return this.redirect(String.format("/products/%s#reviews", productId));
    }

    @PostMapping("/{id}")
    public CommentServiceModel editComment(@PathVariable String productId, @PathVariable String id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable("productId") String productId, @PathVariable("id") String id) {
        this.commentService.deleteComment(id, productId);

        return this.redirect("/customers/profile/comments");
    }
}
