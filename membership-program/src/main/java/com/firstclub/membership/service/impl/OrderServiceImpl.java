package com.firstclub.membership.service.impl;

import com.firstclub.membership.domain.dto.OrderRequest;
import com.firstclub.membership.domain.entity.User;
import com.firstclub.membership.domain.entity.UserOrder;
import com.firstclub.membership.exception.MembershipException;
import com.firstclub.membership.repository.OrderRepository;
import com.firstclub.membership.repository.UserRepository;
import com.firstclub.membership.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public UserOrder placeOrder(OrderRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new MembershipException("User not found"));
        return orderRepository.save(new UserOrder(user, request.amount()));
    }
}
