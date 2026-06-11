package com.firstclub.membership.service;

import com.firstclub.membership.domain.enums.TierName;

public interface TierEvaluationService {
    TierName evaluateEligibleTier(Long userId);

    boolean meetsThreshold(Long userId, TierName tierName);
}
