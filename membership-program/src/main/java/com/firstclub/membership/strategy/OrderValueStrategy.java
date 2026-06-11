package com.firstclub.membership.strategy;

import com.firstclub.membership.config.TierCriteriaProperties.TierRule;
import com.firstclub.membership.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class OrderValueStrategy implements TierEvaluationStrategy {

    private final OrderRepository orderRepository;

    public OrderValueStrategy(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean isEligible(Long userId, TierRule rule) {
        if (rule == null || rule.getMinOrderValue() == null || rule.getMinOrderValue().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        BigDecimal total = orderRepository.sumOrderValueSince(userId, startOfMonth).orElse(BigDecimal.ZERO);
        return total.compareTo(rule.getMinOrderValue()) >= 0;
    }

    @Override
    public String getStrategyType() {
        return "ORDER_VALUE";
    }
}
