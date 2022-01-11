package bg.softuni.eshop.order.model.entity;

import bg.softuni.eshop.BaseEntity;
import bg.softuni.eshop.order.model.еnums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity extends BaseEntity {
    @Enumerated(STRING)
    private PaymentStatus status;
}
