package bg.softuni.eshop.product.model.binding;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameBindingModel extends ProductBindingModel {

    @Expose
    @NotNull(message = "Company mustn't be null.")
    private String company;

    @Expose
    private String[] platforms;
}
