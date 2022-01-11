package bg.softuni.eshop.product.model.service;

import lombok.Data;

@Data
public class BookServiceModel extends ProductServiceModel {
    private String author;
    private Integer pages;
}
