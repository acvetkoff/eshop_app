package bg.softuni.eshop.product.model.view;

import bg.softuni.eshop.product.model.entity.PlatformEntity;
import lombok.Data;

@Data
public class GameViewModel extends ProductViewModel {
    private String company;
    private String[] platforms;
}
