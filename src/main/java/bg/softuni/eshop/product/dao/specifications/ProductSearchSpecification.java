package bg.softuni.eshop.product.dao.specifications;

import bg.softuni.eshop.product.model.binding.SearchProductCriteriaDTO;
import bg.softuni.eshop.product.model.entity.GameEntity;
import bg.softuni.eshop.product.model.entity.PlatformEntity;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.enums.GamePlatform;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductSearchSpecification implements Specification<Product> {

    private static final long serialVersionUID = 1L;
    private final SearchProductCriteriaDTO searchProductCriteriaDTO;

    public ProductSearchSpecification(SearchProductCriteriaDTO searchProductCriteriaDTO) {
        this.searchProductCriteriaDTO = searchProductCriteriaDTO;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate result = cb.conjunction();

        if (Objects.nonNull(this.searchProductCriteriaDTO.getType())) {
            result = cb.and(result, cb.equal(root.get("type"), this.searchProductCriteriaDTO.getType()));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getTitle())) {
            result = cb.and(result, cb.like(root.get("title"), "%" + this.searchProductCriteriaDTO.getTitle() + "%"));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getDescription())) {
            result = cb.and(result,
                    cb.like(root.get("description"), "%" + this.searchProductCriteriaDTO.getDescription() + "%"));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getStartPrice())) {
            result = cb.and(result,
                    cb.greaterThanOrEqualTo(root.get("price"), this.searchProductCriteriaDTO.getStartPrice()));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getEndPrice())) {
            result = cb.and(result,
                    cb.lessThanOrEqualTo(root.get("price"), this.searchProductCriteriaDTO.getEndPrice()));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getStartDate())
                && Objects.nonNull(this.searchProductCriteriaDTO.getEndDate())
                && this.searchProductCriteriaDTO.getStartDate().isBefore(this.searchProductCriteriaDTO.getEndDate())) {
            result = cb.and(result, cb.between(root.get("releasedOn"), this.searchProductCriteriaDTO.getStartDate(),
                    this.searchProductCriteriaDTO.getEndDate()));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getStartDate())) {
            result = cb.and(result,
                    cb.greaterThanOrEqualTo(root.get("releasedOn"), this.searchProductCriteriaDTO.getStartDate()));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getEndDate())) {
            result = cb.and(result,
                    cb.lessThanOrEqualTo(root.get("releasedOn"), this.searchProductCriteriaDTO.getEndDate()));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getCompany())) {
            Root<GameEntity> gameEntityRoot = cb.treat(root, GameEntity.class);

            result = cb.and(result,
                    cb.like(gameEntityRoot.get("company"), "%" + this.searchProductCriteriaDTO.getCompany() + "%"));
        }

        if (Objects.nonNull(this.searchProductCriteriaDTO.getPlatforms())
                && this.searchProductCriteriaDTO.getPlatforms().size() > 0) {
            Root<GameEntity> gameEntityRoot = cb.treat(root, GameEntity.class);
            Join<GameEntity, PlatformEntity> products = gameEntityRoot.join("platforms");
            result = cb.and(result, products.get("key").in(searchProductCriteriaDTO.getPlatforms().stream().map(GamePlatform::name).collect(Collectors.toList())));
        }

        return result;
    }
}
