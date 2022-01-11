package bg.softuni.eshop.admin.controllers;

import bg.softuni.eshop.BaseController;
import bg.softuni.eshop.admin.service.AdminProductService;
import bg.softuni.eshop.product.dao.ReferencedDataRepository;
import bg.softuni.eshop.product.model.binding.ProductBindingModel;
import bg.softuni.eshop.product.model.entity.GenreEntity;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController extends BaseController {

    private final AdminProductService productService;
    private final ProductService userProductService;
    private final ReferencedDataRepository<GenreEntity> genreRepository;

    protected AdminProductController(ModelParser modelParser, AdminProductService productService, ProductService userProductService, ReferencedDataRepository<GenreEntity> genreRepository) {
        super(modelParser);
        this.productService = productService;
        this.userProductService = userProductService;
        this.genreRepository = genreRepository;
    }

    @PostMapping("/{id}/uploadPoster")
    public String updatePoster(@PathVariable("id") String id,
                                 @RequestParam("poster") MultipartFile multipartFile) throws IOException {

        this.productService.uploadPoster(id, multipartFile.getOriginalFilename(), multipartFile.getInputStream());
        return this.redirect("/admin/products/%s", id);
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable String id, Model model) {
        ProductServiceModel productServiceModel = this.productService.getById(id);
        ProductViewModel product = this.map(productServiceModel, ProductViewModel.class);

        model.addAttribute("product", product);
        return this.view("/admin/product/product-details");
    }

    @GetMapping("/add")
    public String getAdd(Model model) throws IOException {
        model.addAttribute("productBindingModel", new ProductBindingModel());
        model.addAttribute("predefinedGenres", genreRepository.findAll());
        model.addAttribute("types", userProductService.getProductTypes());
        return this.view("admin/product/add-product");
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("productBindingModel") @Valid ProductBindingModel productBindingModel,
                            @RequestParam("file") MultipartFile multipartFile) throws IOException {
        ProductServiceModel product = this.map(productBindingModel, ProductServiceModel.class);
        product = this.productService.add(product);
        productService.uploadPoster(product.getId(), multipartFile.getOriginalFilename(), multipartFile.getInputStream());

        return this.redirect("/admin/products/%s", product.getId());
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductBindingModel> edit(@RequestBody ProductBindingModel productBindingModel, @PathVariable String id) {
        this.productService.edit(id, this.map(productBindingModel, ProductServiceModel.class));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ProductBindingModel> patch(@RequestBody ProductBindingModel productBindingModel, @PathVariable String id) {
        ProductServiceModel productServiceModel = this.map(productBindingModel, ProductServiceModel.class);

        this.productService.patch(id, productServiceModel);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.productService.deleteById(id);
        return this.redirect("/products", id);
    }
}
