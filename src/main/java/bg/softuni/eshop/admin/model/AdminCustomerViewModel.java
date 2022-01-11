package bg.softuni.eshop.admin.model;

import bg.softuni.eshop.BaseServiceModel;
import lombok.Data;

@Data
public class AdminCustomerViewModel extends BaseServiceModel {

	private String firstName;

	private String lastName;

	private String phone;

	private String address;

}
