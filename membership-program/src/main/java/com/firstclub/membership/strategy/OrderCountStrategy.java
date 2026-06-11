package com.firstclub.membership.strategy;

import com.firstclub.membership.config.TierCriteriaProperties.TierRule;
import com.firstclub.membership.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCountStrategy implements TierEvaluationStrategy {

    private final OrderRepository orderRepository;

    public OrderCountStrategy(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isEligible(Long userId, TierRule rule) {
        if (rule == null || rule.getMinOrderCount() <= 0) {
            return false;
        }
        return orderRepository.countByUser_Id(userId) >= rule.getMinOrderCount();
    }

    @Override
    public String getStrategyType() {
        return "ORDER_COUNT";
    }
}
