package bg.softuni.eshop.product.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final CommentService commentService;

    protected ProductController(ModelParser modelParser, ProductService productService, CommentService commentService, CurrentlyLoggedInUser currentlyLoggedInUser) {
        super(modelParser);
        this.productService = productService;
        this.commentService = commentService;
    }

    @GetMapping
    public String viewProductsByType(@RequestParam String type, Model model) {
        model.addAttribute("currentPageURI", "/products?type=" + type);

        if (!model.containsAttribute("products")) {
            List<ProductViewModel> productViewModels =
                    this.map(this.productService.getByType(type), ProductViewModel.class);

            model.addAttribute("products", productViewModels);
        }

        model.addAttribute("type", type);

        return this.view("product/products");
    }

    @PostMapping
    public String sort(@RequestParam String type, String sortCriteria, RedirectAttributes redirectAttributes) {
        List<ProductViewModel> productViewModels =
                this.map(this.productService.sortBy(type, sortCriteria), ProductViewModel.class);

        redirectAttributes.addFlashAttribute("products", productViewModels);

        return this.redirect("/products?type=" + type);
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable String id, Model model) {
        model.addAttribute("currentPageURI", "/products/" + id);
        ProductServiceModel productServiceModel = this.productService.getById(id);
        ProductViewModel product = this.map(productServiceModel, ProductViewModel.class);

        List<CommentServiceModel> commentServiceModels = this.commentService.getComments(id);
        List<CommentViewModel> comments = this.map(commentServiceModels, CommentViewModel.class);

        // TODO: improve this functionality
        comments.forEach(comment ->
                comment.setDaysPassed(this.commentService.getDaysPassed(comment.getCreatedOn())));

        List<ProductViewModel> relatedProducts =
                this.map(this.productService.getByGenresKeyIn(product.getGenres()), ProductViewModel.class);

        model.addAttribute("type", product.getType());
        model.addAttribute("product", product);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedProducts", relatedProducts);

        return this.view("product/product-details");
    }

    @DeleteMapping("/{productId}/deleteComment/{commentId}")
    public String deleteProductReview(@PathVariable("commentId") String commentId,
                                      @PathVariable("productId") String productId) {

        this.commentService.deleteComment(commentId, productId);

        return this.redirect("/products/" + productId);
    }
}
