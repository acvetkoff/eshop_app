package bg.softuni.eshop.product.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "books")
@PrimaryKeyJoinColumn(name = "book_id")
public class BookEntity extends Product {

    @Column(name = "author", updatable = false)
    private String author;

    @Column(name = "pages")
    private Integer pages;
}
