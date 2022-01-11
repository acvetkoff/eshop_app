package bg.softuni.eshop.user.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.user.dao.CommentRepository;
import bg.softuni.eshop.user.model.entity.CommentEntity;
import bg.softuni.eshop.user.model.service.CommentServiceModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class CommentServiceImpl extends BaseService implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(ModelParser modelParser, CommentRepository commentRepository) {
        super(modelParser);
        this.commentRepository = commentRepository;
    }

    @Override
    public void addComment(CommentServiceModel comment) {
        CommentEntity commentEntity = this.map(comment, CommentEntity.class);
        this.commentRepository.save(commentEntity);
    }

    @Override
    public void editComment(CommentServiceModel comment, String commentId) {
        // TODO
    }

    @Override
    @Transactional
    public void deleteComment(String commentId, String productId) {
        this.commentRepository.deleteByIdAndProductId(commentId, productId);
    }

    @Override
    public List<CommentServiceModel> getComments(String productId) {
        return this.map(this.commentRepository.getAllByProductId(productId), CommentServiceModel.class);
    }

    @Override
    public List<CommentServiceModel> getComments() {
        return this.map(this.commentRepository.findAll(), CommentServiceModel.class);
    }

    @Override
    public Integer getDaysPassed(LocalDate createdOn) {
        Period period = Period.between(LocalDate.now(), createdOn);
        return period.getDays();
    }
}
