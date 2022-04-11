package bg.softuni.eshop.product.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.exceptions.ResourceNotFoundException;
import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.dao.specifications.ProductSearchSpecification;
import bg.softuni.eshop.product.model.binding.SearchProductCriteriaDTO;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static bg.softuni.eshop.common.ExceptionMessages.PRODUCT_NOT_FOUND_MESSAGE;
import static bg.softuni.eshop.product.model.enums.ProductType.getSupportedTypes;
import static bg.softuni.eshop.utils.providers.ProductSpecificationProvider.greaterThan;
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
        return this.productRepository
                .findAll()
                .stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GameServiceModel> getAllOrderedByTitle() {
        return this.productRepository
                .findAllOrderedByTitle()
                .stream()
                .map(product -> this.map(product, GameServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductType> getProductTypes() {
        return getSupportedTypes();
    }

    @Override
    public List<ProductServiceModel> getByType(String productType) {
        return this.productRepository
                .findByType(ProductType.valueOf(productType))
                .stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> searchProducts(SearchProductCriteriaDTO searchProductCriteriaDTO) {
        return this.productRepository
                .findAll(new ProductSearchSpecification(searchProductCriteriaDTO))
                .stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> findTop6() {
        return this.productRepository.
                findAll(greaterThan(new BigDecimal("30"), true), PageRequest.of(0, 6))
                .map(product -> this.map(product, ProductServiceModel.class))
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByGenresKeyIn(String[] genres) {
        List<Product> products = this.productRepository.findDistinctByGenresKeyIn(Arrays.asList(genres), PageRequest.of(0, 6));
        return this.map(products, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> sortBy(String type, String criteria) {
        if (criteria.equalsIgnoreCase("price")) {
            return this.map(this.productRepository.findAllByTypeSortedByPrice(ProductType.valueOf(type)), ProductServiceModel.class);
        }

        //TODO: fix sorting, make criteria dynamic to avoid if checks
        return null;
    }

    @Override
    public ProductServiceModel getById(String id) {
        return this.productRepository
                .findById(id)
                .map(product -> this.map(product, ProductServiceModel.class))
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<ProductServiceModel> getByPrice(BigDecimal price) {
        return this.productRepository
                .findByPrice(price)
                .stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByTitle(String title) {
        return this.productRepository
                .findByTitle(title).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByDescription(String description) {
        return this.productRepository
                .findByDescription(description).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductServiceModel> getByPriceInRange(BigDecimal start, BigDecimal end) {
        return this.productRepository
                .findAll(priceBetween(start, end)).stream()
                .map(product -> this.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }
}
