package bg.softuni.eshop.repository.products;

import bg.softuni.eshop.user.dao.CustomerRepository;
import bg.softuni.eshop.user.dao.UserRepository;
import bg.softuni.eshop.user.model.entity.CustomerEntity;
import bg.softuni.eshop.user.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private CustomerEntity customer;

    @BeforeEach
    void setUp() {
        this.user = new UserEntity();
        user.setUsername("username");
        user.setPassword("123");
        user.setEmail("username@domain.com");

        this.customer = new CustomerEntity();
        customer.setFirstName("First name");
        customer.setLastName("Last name");
        customer.setPhone("359 885 67 87 31");
        customer.setUser(user);

        user.setCustomer(customer);

        this.userRepository.save(user);
    }

    @Test
    void givenValidUser_ShouldReturnNonEmptyOptional() {
        Optional<CustomerEntity> customerEntityOptional = this.customerRepository
                .findByUserUsername(this.user.getUsername());

        assertNotNull(customerEntityOptional);
    }

    @Test
    void givenInvalidUser_ShouldReturnEmptyOptional() {
        UserEntity fakeUser = new UserEntity();
        fakeUser.setUsername("asd");
        Optional<CustomerEntity> customerEntityOptional = this.customerRepository
                .findByUserUsername(fakeUser.getUsername());

        assertThat(customerEntityOptional.isEmpty()).isEqualTo(true);
    }

    @Test
    void givenValidUser_ShouldReturnCorrectCustomer() {
        this.customerRepository.save(this.customer);

        Optional<CustomerEntity> customerOpt = this.customerRepository.findByUserUsername(this.user.getUsername());

        assertThat(customerOpt.get().getFirstName()).isEqualTo("First name");
    }
}