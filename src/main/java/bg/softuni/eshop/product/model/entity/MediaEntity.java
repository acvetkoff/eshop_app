package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.product.model.enums.ReferencedDataType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import static bg.softuni.eshop.product.model.enums.ReferencedDataType.MEDIA;

@Entity
@DiscriminatorValue(value = "MEDIA")
@Table(name = "media_types")
public class MediaEntity extends ReferencedData {
    public MediaEntity(String key, String value) {
        super(key, value, MEDIA);
    }

    public MediaEntity() {
      this.setType(MEDIA);
    }
}
