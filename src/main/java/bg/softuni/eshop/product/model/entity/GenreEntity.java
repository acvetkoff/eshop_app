package bg.softuni.eshop.product.model.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import static bg.softuni.eshop.product.model.enums.ReferencedDataType.GENRE;

@Entity
@Table(name = "genres")
@DiscriminatorValue(value = "GENRE")
public class GenreEntity extends ReferencedData {
    public GenreEntity(String key, String value) {
        super(key, value, GENRE);
    }

    public GenreEntity() {
        this.setType(GENRE);
    }
}
