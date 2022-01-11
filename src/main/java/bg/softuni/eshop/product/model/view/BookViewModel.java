package bg.softuni.eshop.product.model.view;

import lombok.Data;

@Data
public class BookViewModel extends ProductViewModel {

    private String author;
    private Integer pages;
}
