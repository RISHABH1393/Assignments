package com.firstclub.membership.service;

import com.firstclub.membership.domain.dto.OrderRequest;
import com.firstclub.membership.domain.entity.UserOrder;

public interface OrderService {
    UserOrder placeOrder(OrderRequest request);
}
