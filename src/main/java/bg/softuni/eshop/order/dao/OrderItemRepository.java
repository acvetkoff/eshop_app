package bg.softuni.eshop.order.dao;

import bg.softuni.eshop.order.model.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, String> {

    List<OrderItemEntity> findByProductId(String productId);
}
