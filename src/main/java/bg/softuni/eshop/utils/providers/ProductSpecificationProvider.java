package bg.softuni.eshop.utils.providers;

import bg.softuni.eshop.product.model.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.springframework.data.jpa.domain.Specification.where;

public class ProductSpecificationProvider {

    public static Specification<Product> priceBetween(BigDecimal first, BigDecimal second) {
        return where(greaterThanOrEqual(first))
                .and(lessThanOrEqual(second));
    }

    public static Specification<Product> lessThan(BigDecimal price, boolean isInclusive) {
        if (isInclusive) {
            return where(lessThanOrEqual(price));
        }

        return (root, cq, cb) -> cb.lessThan(root.get("price"), price);
    }

    public static Specification<Product> greaterThan(BigDecimal price, boolean isInclusive) {
        if (isInclusive) {
            return where(greaterThanOrEqual(price));
        }

        return (root, cq, cb) -> cb.greaterThan(root.get("price"), price);
    }

    private static Specification<Product> greaterThanOrEqual(BigDecimal price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }

    private static Specification<Product> lessThanOrEqual(BigDecimal price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }
}
