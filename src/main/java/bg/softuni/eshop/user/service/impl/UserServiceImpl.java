package bg.softuni.eshop.user.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.common.ExceptionMessages;
import bg.softuni.eshop.exceptions.ValidationException;
import bg.softuni.eshop.exceptions.RoleNotFoundException;
import bg.softuni.eshop.user.dao.CustomerRepository;

import bg.softuni.eshop.exceptions.UserAlreadyPersistedException;
import bg.softuni.eshop.user.dao.UserRepository;
import bg.softuni.eshop.user.dao.UserRoleRepository;
import bg.softuni.eshop.user.model.entity.CustomerEntity;
import bg.softuni.eshop.user.model.entity.UserEntity;
import bg.softuni.eshop.user.model.entity.UserRoleEntity;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.HashSet;

import static bg.softuni.eshop.common.ExceptionMessages.*;
import static bg.softuni.eshop.user.model.enums.UserRole.USER;

@Service
@Transactional
public class UserServiceImpl extends BaseService implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final CustomerRepository customerRepository;

    @Autowired
    public UserServiceImpl(ModelParser modelParser, UserRoleRepository userRoleRepository,
                           UserRepository userRepository, PasswordEncoder passwordEncoder,
                           ApplicationUserService applicationUserService, CustomerRepository customerRepository) {
        super(modelParser);
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserServiceModel getByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(user -> this.map(user, UserServiceModel.class)).orElseThrow(
                        () -> new UsernameNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND, username)));
    }

    @Override
    @Transactional
    public void registerAndLogin(UserServiceModel userServiceModel) {
        UserEntity user = this.map(userServiceModel, UserEntity.class);
        user = this.registerUser(userServiceModel, user);
        this.loginUser(user);

        // needed for test - to be discussed
        userServiceModel.setId(user.getId());
    }


    @Override
    public boolean isPasswordValid(String username, String givenPassword) {
        String validPassword = this.userRepository.findPassword(username);
        String encodedGivenPass = this.passwordEncoder.encode(givenPassword);
        return validPassword.equals(encodedGivenPass);
    }

    @Override
    public boolean exists(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public void ensureCredentials(BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
    }

    private UserEntity registerUser(@Valid UserServiceModel userServiceModel, UserEntity user) {
        this.checkIfUserExists(userServiceModel.getUsername(), userServiceModel.getEmail());

        user.setRoles(new HashSet<>());
        user.setPassword(this.passwordEncoder.encode(userServiceModel.getPassword()));

        UserRoleEntity userRole = this.userRoleRepository
                .findByUserRole(USER)
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format(ExceptionMessages.ROLE_NOT_FOUND_MESSAGE, USER.name())));

        user.addRole(userRole);


        if (user.getCustomer() == null) {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setFirstName(userServiceModel.getUsername());
            user.setCustomer(customerEntity);
        }

        user = this.userRepository.save(user);
        return user;
    }

    @Override
    public void loginUser(String username, String password) {
        UserDetails principal = this.applicationUserService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                password,
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void loginUser(UserEntity user) {
        UserDetails principal = this.applicationUserService.loadUserByUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                user.getPassword(),
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void checkIfUserExists(String username, String email) {
        if (this.userRepository.existsByUsername(username)) {
            throw new UserAlreadyPersistedException(USER_ALREADY_EXIST_MESSAGE);
        }
        if (this.userRepository.existsByUsername(email)) {
            throw new UserAlreadyPersistedException(USER_EMAIL_ALREADY_EXIST_MESSAGE);
        }
    }

    @Override
    public void updateProfile(UserServiceModel userServiceModel) {
        userRepository.save(map(userServiceModel, UserEntity.class));
    }
}
