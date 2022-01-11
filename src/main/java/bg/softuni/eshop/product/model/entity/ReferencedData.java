package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.product.model.enums.ReferencedDataType;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public class ReferencedData extends BaseEntity {

    @Expose
    @Column
    @Enumerated(STRING)
    private ReferencedDataType type;

    @Expose
    @Column(name = "data_key", nullable = false, updatable = false)
    private String key;

    @Expose
    @Column(name = "data_value", nullable = false, updatable = false)
    private String value;

    public ReferencedData(String key, String value, ReferencedDataType type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }
}
