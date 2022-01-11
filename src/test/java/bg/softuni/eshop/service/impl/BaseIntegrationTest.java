package bg.softuni.eshop.service.impl;

import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;


public abstract class BaseIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private UserService userService;

	protected UserServiceModel registerAndLogin(String user, String password) {
		UserServiceModel userServiceModel = new UserServiceModel();
		userServiceModel.setUsername(user);
		userServiceModel.setPassword(password);
		userServiceModel.setEmail("test@email.com");
		userServiceModel.setCustomer(new CustomerServiceModel());
		userService.registerAndLogin(userServiceModel);
		return userServiceModel;
	}
}
