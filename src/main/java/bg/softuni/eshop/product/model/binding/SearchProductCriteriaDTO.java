package bg.softuni.eshop.product.model.binding;

import bg.softuni.eshop.product.model.enums.GamePlatform;
import bg.softuni.eshop.product.model.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
public class SearchProductCriteriaDTO {

    private ProductType type = ProductType.GAME;
    private String title;
    private String description;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private LocalDate startDate;
    private LocalDate endDate;

    // GameEntity fields
    private String company;
    private Set<GamePlatform> platforms;
}
