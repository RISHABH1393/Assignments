package com.firstclub.membership.service.impl;

import com.firstclub.membership.config.TierCriteriaProperties;
import com.firstclub.membership.config.TierCriteriaProperties.TierRule;
import com.firstclub.membership.domain.enums.TierName;
import com.firstclub.membership.exception.MembershipException;
import com.firstclub.membership.repository.UserRepository;
import com.firstclub.membership.service.TierEvaluationService;
import com.firstclub.membership.strategy.TierEvaluationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TierEvaluationServiceImpl implements TierEvaluationService {

    private final List<TierEvaluationStrategy> strategies;
    private final TierCriteriaProperties tierCriteriaProperties;
    private final UserRepository userRepository;

    public TierEvaluationServiceImpl(List<TierEvaluationStrategy> strategies,
                                     TierCriteriaProperties tierCriteriaProperties,
                                     UserRepository userRepository) {
        this.strategies = strategies;
        this.tierCriteriaProperties = tierCriteriaProperties;
        this.userRepository = userRepository;
    }

    @Override
    public TierName evaluateEligibleTier(Long userId) {
        validateUserExists(userId);
        for (TierName tierName : new TierName[]{TierName.PLATINUM, TierName.GOLD, TierName.SILVER}) {
            if (meetsThreshold(userId, tierName)) {
                return tierName;
            }
        }
        return TierName.SILVER;
    }

    @Override
    public boolean meetsThreshold(Long userId, TierName tierName) {
        validateUserExists(userId);
        TierRule rule = tierCriteriaProperties.getRuleForTier(tierName.name());
        if (rule == null) {
            return false;
        }
        return strategies.stream().anyMatch(strategy -> strategy.isEligible(userId, rule));
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new MembershipException("User not found");
        }
    }
}
