package bg.softuni.eshop.filters;

import bg.softuni.eshop.exceptions.UserAlreadyPersistedException;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.user.service.impl.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class Oauth2AuthorizationFilter extends GenericFilterBean {

    private final UserService userService;

    @Autowired
    public Oauth2AuthorizationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User principal = ((DefaultOAuth2User) context.getAuthentication().getPrincipal());

            UserServiceModel newUser = new UserServiceModel();
            newUser.setEmail(principal.getAttributes().get("email").toString());
            newUser.setPassword(UUID.randomUUID().toString());
            newUser.setRoles(new String[]{"USER"});
            newUser.setUsername(newUser.getEmail());
            newUser.setCustomer(new CustomerServiceModel());
            newUser.getCustomer().setFirstName(principal.getAttribute("given_name"));
            newUser.getCustomer().setLastName(principal.getAttribute("family_name"));
            newUser.getCustomer().setAddress("DEFAULT");
            newUser.getCustomer().setPhone("DEFAULT");

            try {
                userService.registerAndLogin(newUser);
            } catch (UserAlreadyPersistedException e) {
                userService.loginUser(newUser.getEmail(), newUser.getPassword());
            }
        }

        chain.doFilter(request, response);
    }
}