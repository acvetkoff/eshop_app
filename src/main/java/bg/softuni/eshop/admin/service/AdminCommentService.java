package bg.softuni.eshop.admin.service;

import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.model.service.UserServiceModel;

import java.util.List;

// TODO add pagination
public interface AdminCommentService {

    List<CommentServiceModel> findAllComments();

    List<CommentServiceModel> findAllCommentsForUser(String userId);

    void deleteComment(String commentId);

    void deleteByFrom(String id);
}
