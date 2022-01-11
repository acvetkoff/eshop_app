package bg.softuni.eshop.user.service;

import bg.softuni.eshop.user.model.service.CommentServiceModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    void addComment(CommentServiceModel comment);

    void editComment(CommentServiceModel comment, String commentId);

    void deleteComment(String commentId, String productId);

    List<CommentServiceModel> getComments(String productId);

    List<CommentServiceModel> getComments();

    Integer getDaysPassed(LocalDate createdOn);
}
