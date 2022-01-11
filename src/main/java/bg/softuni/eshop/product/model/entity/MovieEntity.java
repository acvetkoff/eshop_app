package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.product.model.enums.ProductType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

import static bg.softuni.eshop.product.model.enums.ProductType.MOVIE;

@Data
@Entity
@Table(name = "movies")
public class MovieEntity extends Product {

    @Column(name = "director")
    private String director;

    @Column(name = "studio")
    private String studio;

    @Column(name = "imdb_url")
    private String imdbUrl;

    @ManyToMany
    private Set<MediaEntity> mediaTypes;

    public MovieEntity() {
        this.setType(MOVIE);
    }

    @Override
    public ProductType getType() {
        return MOVIE;
    }

    public void addMediaType(MediaEntity mediaEntity) {
        this.mediaTypes.add(mediaEntity);
    }
}
