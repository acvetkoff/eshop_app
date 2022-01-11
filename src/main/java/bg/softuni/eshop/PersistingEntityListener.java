package bg.softuni.eshop;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersistingEntityListener {
    @PrePersist
    @PreUpdate
    public void beforeSave(final BaseEntity baseEntity) {
        baseEntity.setCreatedOn(LocalDate.now());

        baseEntity.setCreatedBy("test");
    }
}
