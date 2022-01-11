package bg.softuni.eshop.product.model.view;

import lombok.Data;

@Data
public class MovieViewModel extends ProductViewModel {

    private String director;
    private String imdbUrl;
    private String studio;
}
