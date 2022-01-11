//package bg.softuni.eshop.user.controllers;
//
//import bg.softuni.eshop.utils.parsers.ModelParser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/test")
//public class TestController {
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ModelParser modelParser;
//
//    @Autowired
//    private PlatformRepository platformRepository;
//
//    @GetMapping()
//    public List<ProductViewModel> test() {
//        SearchProductCriteriaDTO criteria = new SearchProductCriteriaDTO();
//        criteria.setType(ProductType.GAME);
//        criteria.setPlatforms(new GamePlatform[] { GamePlatform.PC, GamePlatform.PS} );
//
//        List<ProductViewModel> products = this.productService.getByCriteria(criteria)
//                .stream()
//                .map(productServiceModel -> this.modelParser.convert(productServiceModel, ProductViewModel.class))
//                .collect(Collectors.toList());
//
//        return products;
//    }
//
//    @GetMapping("/price")
//    public List<ProductViewModel> getByPrice() {
//        List<ProductViewModel> products = this.productService
//                .getByPrice(BigDecimal.valueOf(3.00))
//                .stream()
//                .map(productServiceModel -> this.modelParser.convert(productServiceModel, ProductViewModel.class))
//                .collect(Collectors.toList());
//
//        return products;
//    }
//
//    @GetMapping("/title/{title}")
//    public List<ProductViewModel> getByTitle(@PathVariable String title) {
//        List<ProductViewModel> products = this.productService
//                .getByTitle(title)
//                .stream()
//                .map(productServiceModel -> this.modelParser.convert(productServiceModel, ProductViewModel.class))
//                .collect(Collectors.toList());
//
//        return products;
//    }
//
//    @GetMapping("/desc/{desc}")
//    public List<ProductViewModel> getByDesc(@PathVariable("desc") String description) {
//        List<ProductViewModel> products = this.productService
//                .getByDescription(description)
//                .stream()
//                .map(productServiceModel -> this.modelParser.convert(productServiceModel, ProductViewModel.class))
//                .collect(Collectors.toList());
//
//        return products;
//    }
//
//    @GetMapping("/range/{start}/and/{end}")
//    public List<ProductViewModel> getInRange(@PathVariable("start") BigDecimal start, @PathVariable("end") BigDecimal end) {
//        List<ProductViewModel> products = this.productService
//                .getByPriceInRange(start, end)
//                .stream()
//                .map(productServiceModel -> this.modelParser.convert(productServiceModel, ProductViewModel.class))
//                .collect(Collectors.toList());
//
//        return products;
//    }
//}
