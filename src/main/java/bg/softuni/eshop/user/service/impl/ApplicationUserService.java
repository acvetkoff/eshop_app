package bg.softuni.eshop.user.service.impl;

import bg.softuni.eshop.user.dao.UserRepository;
import bg.softuni.eshop.user.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static bg.softuni.eshop.common.ExceptionMessages.USER_NOT_FOUND;
import static bg.softuni.eshop.common.GlobalConstants.ROLE_PREFIX;


@Service
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));

        return this.mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {
        List<GrantedAuthority> authorities = userEntity
                .getRoles()
                .stream()
                .map(userRoleEntity -> new SimpleGrantedAuthority(ROLE_PREFIX + userRoleEntity.getUserRole().name()))
                .collect(Collectors.toList());

        UserDetails user = User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .disabled(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .authorities(authorities)
                .build();

        return user;
    }
}
