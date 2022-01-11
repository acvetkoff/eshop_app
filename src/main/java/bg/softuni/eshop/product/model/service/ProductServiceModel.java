package bg.softuni.eshop.product.model.service;

import bg.softuni.eshop.BaseServiceModel;
import bg.softuni.eshop.product.model.entity.GenreEntity;
import bg.softuni.eshop.product.model.entity.ImageEntity;
import bg.softuni.eshop.product.model.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductServiceModel extends BaseServiceModel {
    private String title;
    private ProductType type;
    private BigDecimal price;
    private String description;
    private LocalDate releasedOn;
    private String[] genres;
    private ImageEntity image;
}

