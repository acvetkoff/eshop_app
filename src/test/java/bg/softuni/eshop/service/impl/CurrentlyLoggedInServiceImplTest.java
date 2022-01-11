package bg.softuni.eshop.service.impl;

import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.model.entity.BookEntity;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.view.CommentViewModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.user.model.entity.CommentEntity;
import bg.softuni.eshop.user.model.entity.CustomerEntity;
import bg.softuni.eshop.user.model.entity.UserEntity;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.CustomerServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.user.service.CurrentlyLoggedInUser;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class CurrentlyLoggedInServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private CurrentlyLoggedInUser currentlyLoggedInUser;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelParser modelParser;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void loggedInCustomer_ShouldReturnCorrectCustomer() {
        UserServiceModel userServiceModel = this.registerAndLogin("User", "Password");

        userServiceModel.getCustomer().setFirstName("First name");
        userServiceModel.getCustomer().setLastName("Last name");
        userServiceModel.getCustomer().setUser(userServiceModel);

        userService.updateProfile(userServiceModel);

        CustomerServiceModel customerServiceModel = this.currentlyLoggedInUser.loggedInCustomer();

        assertEquals("First name", customerServiceModel.getFirstName());
    }

    @Test
    void loggedInCustomer_addedCommentsShouldAddCorrectly() {
        UserServiceModel userServiceModel = this.registerAndLogin("User", "Password");
        Product product = productRepository.findAllOrderedByTitle().get(0);
        userServiceModel.getCustomer().setFirstName("First name");
        userServiceModel.getCustomer().setLastName("Last name");
        userServiceModel.getCustomer().setUser(userServiceModel);

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText("Comment");
        commentEntity.setFrom(this.modelParser.convert(userServiceModel, UserEntity.class));
        commentEntity.setProduct(product);

        this.commentService.addComment(this.modelParser.convert(commentEntity, CommentServiceModel.class));
        List<CommentServiceModel> comments = this.currentlyLoggedInUser.getCustomerComments(userServiceModel.getId());

        assertEquals(1, comments.size());
    }
}