package bg.softuni.eshop.user.dao;

import bg.softuni.eshop.user.model.entity.CommentEntity;
import bg.softuni.eshop.user.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {

    List<CommentEntity> getAllByProductIdAndFromId(String productId, String fromId);

    List<CommentEntity> getAllByProductId(String productId);

    List<CommentEntity> getAllByFromId(String userId);

    void deleteByIdAndProductId(String id, String productId);

    void deleteByFromId(String id);

    List<CommentEntity> findByFromId(String userId);
}
