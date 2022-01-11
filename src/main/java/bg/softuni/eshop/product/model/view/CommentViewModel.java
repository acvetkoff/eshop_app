package bg.softuni.eshop.product.model.view;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentViewModel {
    private String id;
    private String fromUsername;
    private ProductViewModel product;
    private String text;
    private LocalDate createdOn;
    private Integer daysPassed;
}
