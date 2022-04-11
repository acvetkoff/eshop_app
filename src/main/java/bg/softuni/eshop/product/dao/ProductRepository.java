package bg.softuni.eshop.product.dao;

import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.enums.ProductType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p ORDER BY p.title ASC")
    List<Product> findAllOrderedByTitle();

    @Query("SELECT p FROM Product p WHERE p.type = :type ORDER BY p.releasedOn DESC")
    List<Product> findByType(@Param(value = "type") ProductType type);

    @Query("SELECT DISTINCT(p.type) FROM Product p ")
    List<String> findProductTypes();

    @Query("SELECT p FROM Product p WHERE p.type = :type ORDER BY p.price ASC")
    List<Product> findAllByTypeSortedByPrice(@Param("type") ProductType type);

    List<Product> findByPrice(BigDecimal price);

    @Query("SELECT p FROM Product p " +
            "WHERE p.title LIKE CONCAT('%', :title, '%')")
    List<Product> findByTitle(@Param("title") String title);

    @Query("SELECT p FROM Product p " +
            "WHERE p.description LIKE CONCAT('%', :description, '%')")
    List<Product> findByDescription(@Param("description") String desc);

    @Query("SELECT p FROM Product p " +
            "WHERE p.price BETWEEN :startPrice AND :endPrice")
    List<Product> findAllByPriceInRange(@Param("startPrice") BigDecimal startPrice,@Param("endPrice") BigDecimal endPrice);

    List<Product> findAllByPriceGreaterThan(BigDecimal price);

    List<Product> findDistinctByGenresKeyIn(@Param("keys") Collection<String> keys, Pageable pageable);
}
