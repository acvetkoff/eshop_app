package bg.softuni.eshop.order.dao;

import bg.softuni.eshop.order.model.entity.OrderEntity;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

	List<OrderEntity> findAllByUserId(String userId);

	List<OrderEntity> findAllByUserIdAndStatus(String userId, OrderStatus status);

	List<OrderEntity> findAllByStatus(OrderStatus status);

	List<OrderEntity> findAllByItemsProductId(String productId);

    void deleteByUserId(String id);

	boolean existsById(String id);
}
