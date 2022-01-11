package bg.softuni.eshop.product.model.binding;

import bg.softuni.eshop.product.model.entity.GenreEntity;
import bg.softuni.eshop.product.model.entity.ImageEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@JsonTypeInfo(use = NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookBindingModel.class, name = "BOOK"),
        @JsonSubTypes.Type(value = GameBindingModel.class, name = "GAME"),
        @JsonSubTypes.Type(value = MovieBindingModel.class, name = "MOVIE")
})
@AllArgsConstructor
@NoArgsConstructor
public class ProductBindingModel {

    private String id;

    @Expose
    private String type;

    @Expose
    private String title;

    @Expose
    private BigDecimal price;

    @Expose
    private String description;

    @Expose
    private LocalDate releasedOn;

    @Expose
    private String[] genres;

    @Expose
    private ImageEntity image;
}
