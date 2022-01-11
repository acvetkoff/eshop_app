package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.user.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static bg.softuni.eshop.product.model.enums.ProductType.DEFAULT;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.InheritanceType.JOINED;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Inheritance(strategy = JOINED)
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(STRING)
    @Column(updatable = false)
    private ProductType type;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToMany
    private Set<GenreEntity> genres;

    @Column(name = "released_on", updatable = false)
    private LocalDate releasedOn;

    @Transient
    private boolean inStock = true;

    @Transient
    private Integer quantity = 0;

    @OneToOne(cascade = ALL, fetch = EAGER)
    private ImageEntity image;

    public Product() {
        this.setType(DEFAULT);
    }

    public void addGenre(GenreEntity genreEntity) {
        this.genres.add(genreEntity);
    }
}
