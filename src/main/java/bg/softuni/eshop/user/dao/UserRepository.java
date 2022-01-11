package bg.softuni.eshop.user.dao;

import bg.softuni.eshop.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

     @Query("SELECT u FROM UserEntity u WHERE u.username = :username or u.email = :username")
     Optional<UserEntity> findByUsername(String username);

     @Query("SELECT count(u.id)>0 FROM UserEntity u WHERE u.username = :username or u.email = :username")
     boolean existsByUsername(String username);

     @Query("SELECT u.password FROM UserEntity u WHERE u.username = :username")
     String findPassword(@Param("username") String username);
}