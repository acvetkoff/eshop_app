package bg.softuni.eshop.user.model.entity;

import bg.softuni.eshop.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "customers")
public class CustomerEntity extends BaseEntity {

    @Column(length = 30)
    private String firstName;

    @Column(length = 30)
    private String lastName;

    @Column(length = 50)
    private String phone;

    private String address;

    @OneToOne(mappedBy = "customer")
    private UserEntity user;
}
