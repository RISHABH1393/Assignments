package com.firstclub.membership.strategy;

import com.firstclub.membership.config.TierCriteriaProperties.TierRule;

public interface TierEvaluationStrategy {
    boolean isEligible(Long userId, TierRule rule);

    String getStrategyType();
}
