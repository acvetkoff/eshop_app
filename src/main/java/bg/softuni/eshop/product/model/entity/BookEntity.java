package bg.softuni.eshop.product.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "books")
public class BookEntity extends Product {

    @Column(name = "author", updatable = false)
    private String author;

    @Column(name = "pages")
    private Integer pages;
}
