package bg.softuni.eshop.user.service;


import bg.softuni.eshop.exceptions.ValidationException;
import bg.softuni.eshop.user.model.entity.UserEntity;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import org.springframework.validation.BindingResult;

public interface UserService {

	UserServiceModel getByUsername(String username);

	void registerAndLogin(UserServiceModel userServiceModel);

	boolean isPasswordValid(String username, String givenPassword);

	boolean exists(String username);

	void loginUser(String user, String password);

	void updateProfile(UserServiceModel userServiceModel);

	void ensureCredentials(BindingResult bindingResult) throws ValidationException;
}