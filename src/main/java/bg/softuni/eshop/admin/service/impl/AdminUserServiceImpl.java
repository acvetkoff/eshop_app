package bg.softuni.eshop.admin.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.admin.service.AdminCommentService;
import bg.softuni.eshop.admin.service.AdminOrderService;
import bg.softuni.eshop.admin.service.AdminUserService;
import bg.softuni.eshop.exceptions.ProductNotFoundException;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.user.dao.CommentRepository;
import bg.softuni.eshop.user.dao.CustomerRepository;
import bg.softuni.eshop.user.dao.UserRepository;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.user.service.CommentService;
import bg.softuni.eshop.user.service.UserService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl extends BaseService implements AdminUserService {

    private final UserRepository userRepository;
    private final AdminOrderService orderService;
    private final AdminCommentService commentService;
    private final CustomerRepository customerRepository;

    public AdminUserServiceImpl(ModelParser modelParser, UserRepository userRepository, CustomerRepository customerRepository, AdminOrderService orderService, AdminCommentService commentService) {
        super(modelParser);
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.commentService = commentService;
        this.orderService = orderService;
    }

    @Override
    public UserServiceModel findById(String id) {
        return this.userRepository.findById(id)
                .map( (user) -> this.map(user, UserServiceModel.class))
                .orElseThrow();
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map((user) -> this.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.orderService.deleteByUserId(id);
        this.commentService.deleteByFrom(id);
        this.customerRepository.deleteByUserId(id);
        this.userRepository.deleteById(id);
    }
}
