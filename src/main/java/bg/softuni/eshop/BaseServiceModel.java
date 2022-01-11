package bg.softuni.eshop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public abstract class BaseServiceModel {
    private String id;
    private LocalDate createdOn;
}
