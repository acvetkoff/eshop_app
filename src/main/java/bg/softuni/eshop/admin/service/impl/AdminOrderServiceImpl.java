package bg.softuni.eshop.admin.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.admin.service.AdminOrderService;
import bg.softuni.eshop.exceptions.ProductNotFoundException;
import bg.softuni.eshop.order.dao.OrderRepository;
import bg.softuni.eshop.order.model.entity.OrderEntity;
import bg.softuni.eshop.order.model.service.OrderServiceModel;
import bg.softuni.eshop.order.model.еnums.OrderStatus;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminOrderServiceImpl extends BaseService implements AdminOrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public AdminOrderServiceImpl(ModelParser modelParser, OrderRepository orderRepository) {
        super(modelParser);
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderServiceModel findById(String id) {
        return this.map(this.orderRepository.findById(id).orElseThrow(() -> {
            throw new ProductNotFoundException("Order not found " + id);
        }), OrderServiceModel.class);
    }

    @Override
    public List<OrderServiceModel> findAll() {
        return this.map(this.orderRepository.findAll(), OrderServiceModel.class);
    }

    @Override
    public List<OrderServiceModel> findAllByUserId(String userId) {
        return this.map(this.orderRepository.findAllByUserId(userId), OrderServiceModel.class);
    }

    @Override
    @Transactional
    public void delete(String orderId) {
        orderRepository.findById(orderId).ifPresentOrElse(this.orderRepository::delete, () -> {
            throw new ProductNotFoundException("Order not found " + orderId);
        });
    }

    // TODO probably replace with PATCH method
    @Override
    @Transactional
    public OrderServiceModel changeOrderStatus(String id, OrderStatus orderStatus) {
        OrderServiceModel order = findById(id);
        order.setStatus(orderStatus);
        this.orderRepository.save(map(order, OrderEntity.class));
        return order;
    }

    @Override
    @Transactional
    public void deleteByUserId(String id) {
        this.orderRepository.deleteByUserId(id);
    }
}
