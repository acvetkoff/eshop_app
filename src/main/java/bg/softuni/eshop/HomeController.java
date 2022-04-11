package bg.softuni.eshop;

import java.util.List;

import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bg.softuni.eshop.utils.parsers.ModelParser;

import java.util.stream.Collectors;

import static bg.softuni.eshop.product.model.enums.ProductType.*;

@Controller
public class HomeController extends BaseController {

    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public HomeController(ModelParser modelParser, ProductService productService, OrderService orderService) {
    	super(modelParser);
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ProductServiceModel> products = this.productService.findTop6();
        List<ProductServiceModel> games = this.productService.getByType("GAME")
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        List<ProductServiceModel> movies = this.productService.getByType("MOVIE")
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        List<ProductServiceModel> books = this.productService.getByType("BOOK")
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        model.addAttribute("types", this.productService.getProductTypes());
        model.addAttribute("products", products);
        model.addAttribute("games", games);
        model.addAttribute("movies", movies);
        model.addAttribute("books", books);

        return this.view("index");
    }
}