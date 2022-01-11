package bg.softuni.eshop.user.model.binding;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class CustomerBindingModel {

    @Expose
    private String firstName;

    @Expose
    private String lastName;

    @Expose
    private String phone;

    @Expose
    private String address;

    @Expose
    private UserRegisterBindingModel user;
}
