package bg.softuni.eshop.product.model.binding;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class BookBindingModel extends ProductBindingModel {

    @Expose
    private String author;

    @Expose
    private Integer pages;
}
