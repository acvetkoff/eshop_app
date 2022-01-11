package bg.softuni.eshop.product.model.service;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GameServiceModel extends ProductServiceModel {

    @NotNull(message = "Company mustn't be null.")
    private String company;

    private String[] platforms;
}
