package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.BaseEntity;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity extends BaseEntity {

    @Expose
    @Column
    private String imagePath;

    private String fileName;
}
