package bg.softuni.eshop.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.enums.ProductType;
import lombok.Data;

@Configuration
@Data
public class ProductConfiguration {

    @Value("#{${product.types.bindings}}")
    private Map<String, String> productPaths;
    
    @Value("#{${product.types.entities}}")
    private Map<ProductType, Class<? extends Product>> productEntities;

    @Value("${product.posters.public.directory}")
    private String posterPublicDirectory;

    @Value("${product.posters.private.directory}")
    private String posterPrivateDirectory;
}
