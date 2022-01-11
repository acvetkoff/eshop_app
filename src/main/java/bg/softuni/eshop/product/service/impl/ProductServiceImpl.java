package bg.softuni.eshop.product.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.exceptions.ProductNotFoundException;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.dao.specifications.ProductSearchSpecification;
import bg.softuni.eshop.product.model.binding.SearchProductCriteriaDTO;
import bg.softuni.eshop.product.model.entity.GenreEntity;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductConfigurationService;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static bg.softuni.eshop.common.ExceptionMessages.PRODUCT_NOT_FOUND_MESSAGE;
import static bg.softuni.eshop.utils.providers.ProductSpecificationProvider.priceBetween;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ModelParser modelParser, ProductRepository productRepository) {
        super(modelParser);
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductServiceModel> getAll() {
        return this.productRepository.findAll().stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameServiceModel> getAllOrderedByTitle() {
        return this.productRepository.findAllOrderedByTitle().stream()
                .map(product -> this.map(product, GameServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductType> getProductTypes() {
        return ProductType.getSupportedTypes();
    }

    @Override
    public List<ProductServiceModel> getByType(ProductType productType) {
        return this.productRepository
                .findByType(productType)
                .stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> searchProducts(SearchProductCriteriaDTO searchProductCriteriaDTO) {
        return this.productRepository
                .findAll(new ProductSearchSpecification(searchProductCriteriaDTO)).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> findTop6() {
        List<ProductServiceModel> productServiceModels = this.productRepository.findTopByPriceGreaterThan(new BigDecimal("10.00"))
                .stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());

        System.out.println();
        return productServiceModels;
    }
    @Override
    public List<ProductServiceModel> getByGenresKeyIn(String[] genres) {
        List<Product> products = this.productRepository.findDistinctByGenresKeyIn(Arrays.asList(genres), PageRequest.of(0, 6));
        return this.map(products, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel getById(String id) {
        ProductServiceModel productServiceModel = this.productRepository.findById(id)
                .map(product -> this.map(product, ProductServiceModel.class))
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));

        return productServiceModel;
    }

    @Override
    public List<ProductServiceModel> getByPrice(BigDecimal price) {
        return this.productRepository.findByPrice(price).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByTitle(String title) {
        return this.productRepository.findByTitle(title).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByDescription(String description) {
        return this.productRepository.findByDescription(description).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByPriceInRange(BigDecimal start, BigDecimal end) {
        return this.productRepository.findAll(priceBetween(start, end)).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }
}
