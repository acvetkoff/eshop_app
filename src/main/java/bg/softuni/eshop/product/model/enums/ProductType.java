package bg.softuni.eshop.product.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ProductType {
    MOVIE,
    BOOK,
    GAME,
    DEFAULT;

    public static List<ProductType> getSupportedTypes() {
        return Arrays.stream(ProductType.values()).filter(t -> !t.equals(DEFAULT)).collect(Collectors.toList());
    }
}
