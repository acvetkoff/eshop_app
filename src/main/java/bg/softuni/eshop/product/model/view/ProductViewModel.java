package bg.softuni.eshop.product.model.view;

import bg.softuni.eshop.product.model.entity.ImageEntity;
import bg.softuni.eshop.product.model.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductViewModel {
    private String id;
    private String title;
    private ProductType type;
    private BigDecimal price;
    private String description;
    private LocalDate releasedOn;
    private String[] genres;
    private ImageEntity image;
}
