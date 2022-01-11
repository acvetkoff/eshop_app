package bg.softuni.eshop.product.model.service;

import lombok.Data;

@Data
public class MovieServiceModel extends ProductServiceModel {

    private String director;
    private String studio;
    private String imdbUrl;
    private String[] mediaTypes;
}
