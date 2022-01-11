package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.product.model.enums.ReferencedDataType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import static bg.softuni.eshop.product.model.enums.ReferencedDataType.PLATFORM;

@Entity
@Table(name = "platforms")
@DiscriminatorValue(value = "PLATFORM")
public class PlatformEntity extends ReferencedData {
    public PlatformEntity(String key, String value) {
        super(key, value, PLATFORM);
    }

    public PlatformEntity() {
        this.setType(PLATFORM);
    }
}
