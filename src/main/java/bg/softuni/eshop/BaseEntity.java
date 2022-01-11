package bg.softuni.eshop;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EntityListeners(PersistingEntityListener.class)
@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(generator = "string-uuid")
    @GenericGenerator(name = "string-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDate createdOn;

    @Column(nullable = true)
    private String modifiedBy;

    @Column(nullable = true)
    private LocalDate modifiedOn;
}
