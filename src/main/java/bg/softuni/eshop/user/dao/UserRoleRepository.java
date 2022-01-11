package bg.softuni.eshop.user.dao;

import bg.softuni.eshop.user.model.entity.UserRoleEntity;
import bg.softuni.eshop.user.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {

    Optional<UserRoleEntity> findByUserRole(UserRole userRole);
}
