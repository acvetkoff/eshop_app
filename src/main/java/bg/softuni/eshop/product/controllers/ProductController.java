package bg.softuni.eshop.product.controllers;

import java.util.*;

import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.product.model.binding.SearchProductCriteriaDTO;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.utils.parsers.ModelParser;

import javax.servlet.http.HttpServletRequest;

import static bg.softuni.eshop.product.model.enums.ProductType.*;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final CommentService commentService;
    private final CurrentlyLoggedInUser currentlyLoggedInUser;

    protected ProductController(ModelParser modelParser, ProductService productService, CommentService commentService, CurrentlyLoggedInUser currentlyLoggedInUser) {
        super(modelParser);
        this.productService = productService;
        this.commentService = commentService;
        this.currentlyLoggedInUser = currentlyLoggedInUser;
    }

    @GetMapping
    public String viewProductsByType(SearchProductCriteriaDTO searchCriteria, Model model) {
        List<ProductServiceModel> productServiceModels = this.productService.searchProducts(searchCriteria);
        List<ProductViewModel> products = this.map(productServiceModels, ProductViewModel.class);
        String type = searchCriteria.getType().name();
        model.addAttribute("products", products);
        model.addAttribute("type", searchCriteria.getType());

        return this.view("products");
    }

    @PostMapping
    public String filterProducts(String type, RedirectAttributes redirectAttributes) {
        List<ProductServiceModel> productsByType = this.productService.getByType(valueOf(type));
        List<ProductViewModel> productViewModels = this.map(productsByType, ProductViewModel.class);
        redirectAttributes.addFlashAttribute("products", productViewModels);
        redirectAttributes.addFlashAttribute("type", type);
        return this.redirect("products");
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable String id, Model model, HttpServletRequest request) {
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

    @GetMapping("/games")
    public String viewGames(Model model) {
        List<ProductViewModel> games =
                this.map(this.productService.getByType(GAME), ProductViewModel.class);
        model.addAttribute("currentPageURI", "/products/games");
        model.addAttribute("games", games);

        return this.view("product/product-games");
    }


    @GetMapping("/books")
    public String viewBooks(Model model) {
        List<ProductViewModel> books =
                this.map(this.productService.getByType(BOOK), ProductViewModel.class);
        model.addAttribute("currentPageURI", "/products/books");
        model.addAttribute("books", books);

        return this.view("product/product-books");
    }


    @GetMapping("/movies")
    public String viewMovies(Model model) {
        model.addAttribute("currentPageURI", "/products/movies");
        List<ProductViewModel> movies =
                this.map(this.productService.getByType(MOVIE), ProductViewModel.class);

        model.addAttribute("movies", movies);

        return this.view("product/product-movies");
    }

    @DeleteMapping("/{productId}/deleteComment/{commentId}")
    public String deleteProductReview(@PathVariable("commentId") String commentId,
                                      @PathVariable("productId") String productId) {

        this.commentService.deleteComment(commentId, productId);

        return this.redirect("/products/" + productId);
    }
}
