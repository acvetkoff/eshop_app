package bg.softuni.eshop.user.model.service;

import bg.softuni.eshop.BaseServiceModel;
import lombok.Data;

@Data
public class CustomerServiceModel extends BaseServiceModel {

	private String firstName;

	private String lastName;

	private String phone;

	private String address;

	private UserServiceModel user;
}
