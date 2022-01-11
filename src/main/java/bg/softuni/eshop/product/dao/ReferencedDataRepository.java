package bg.softuni.eshop.product.dao;

import bg.softuni.eshop.product.model.entity.ReferencedData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferencedDataRepository<T extends ReferencedData> extends CrudRepository<T, String> {

    Optional<T> findByKey(String key);
}
