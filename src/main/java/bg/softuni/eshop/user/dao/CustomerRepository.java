package bg.softuni.eshop.user.dao;

import bg.softuni.eshop.user.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {

    Optional<CustomerEntity> findByUserUsername(String username);

    void deleteByUserId(String id);
}
