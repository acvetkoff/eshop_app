package bg.softuni.eshop.user.model.view;

import lombok.Data;

@Data
public class CustomerProfileViewModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String userUsername;
}
