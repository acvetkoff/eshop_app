package bg.softuni.eshop.admin.service;

import bg.softuni.eshop.product.model.service.ProductServiceModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AdminProductService {

    ProductServiceModel getById(String id);

    List<ProductServiceModel> findAll();

    void uploadPoster(String id, String fileName, InputStream content) throws IOException;

    ProductServiceModel add(ProductServiceModel productServiceModel);

    void edit(String id, ProductServiceModel productServiceModel);

    void deleteById(String id);

    void patch(String id, ProductServiceModel productServiceModel);

}
