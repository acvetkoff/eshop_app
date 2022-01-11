package bg.softuni.eshop.admin.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.admin.service.AdminCommentService;
import bg.softuni.eshop.user.dao.CommentRepository;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCommentServiceImpl extends BaseService implements AdminCommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public AdminCommentServiceImpl(ModelParser modelParser, CommentRepository commentRepository) {
        super(modelParser);
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentServiceModel> findAllComments() {
        return this.map(this.commentRepository.findAll(), CommentServiceModel.class);
    }

    @Override
    public List<CommentServiceModel> findAllCommentsForUser(String userId) {
        return this.map(this.commentRepository.findByFromId(userId), CommentServiceModel.class);
    }

    @Override
    public void deleteComment(String id) {
        this.commentRepository.deleteById(id);
    }

    @Override
    public void deleteByFrom(String id) {
        this.commentRepository.deleteByFromId(id);
    }
}
