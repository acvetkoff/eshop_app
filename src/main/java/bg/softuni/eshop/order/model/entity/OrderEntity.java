package bg.softuni.eshop.order.model.entity;

import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.user.model.entity.UserEntity;
import lombok.Data;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotEmpty
    private String note;

    @NotNull
    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne(cascade = MERGE)
    @NotNull
    private UserEntity user;

    @OneToMany(cascade = ALL, orphanRemoval = true)
    @NotEmpty
    private List<OrderItemEntity> items;

    @OneToMany(cascade = ALL)
    @NotEmpty
    private List<PaymentEntity> payment;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus status;

    public void addOrderItem(OrderItemEntity orderItemEntity) {
        this.items.add(orderItemEntity);
    }
}
