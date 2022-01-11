package bg.softuni.eshop.product.model.binding;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class MovieBindingModel extends ProductBindingModel {

    @Expose
    private String director;

    @Expose
    private String studio;

    @Expose
    private String imdbUrl;

    @Expose
    private String[] mediaTypes;
}
