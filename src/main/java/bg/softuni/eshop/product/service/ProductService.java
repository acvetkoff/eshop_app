package bg.softuni.eshop.product.service;


import bg.softuni.eshop.product.model.binding.SearchProductCriteriaDTO;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    List<ProductServiceModel> getAll();

    List<GameServiceModel> getAllOrderedByTitle();

    List<ProductType> getProductTypes();

    List<ProductServiceModel> getByType(String productType);

    ProductServiceModel getById(String id);

    List<ProductServiceModel> getByPrice(BigDecimal price);

    List<ProductServiceModel> getByTitle(String title);

    List<ProductServiceModel> getByDescription(String description);

    List<ProductServiceModel> getByPriceInRange(BigDecimal start, BigDecimal end);

    List<ProductServiceModel> searchProducts(SearchProductCriteriaDTO searchProductCriteriaDTO);

    List<ProductServiceModel> findTop6();

    List<ProductServiceModel> getByGenresKeyIn(String[] genres);

    List<ProductServiceModel> sortBy(String type, String criteria);
}
