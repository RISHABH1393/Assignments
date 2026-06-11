package com.firstclub.membership.strategy;

import com.firstclub.membership.config.TierCriteriaProperties.TierRule;
import com.firstclub.membership.domain.entity.User;
import com.firstclub.membership.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CohortStrategy implements TierEvaluationStrategy {

    private final UserRepository userRepository;

    public CohortStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isEligible(Long userId, TierRule rule) {
        if (rule == null || rule.getEligibleCohorts() == null || rule.getEligibleCohorts().isEmpty()) {
            return false;
        }
        return userRepository.findById(userId)
                .map(User::getCohort)
                .map(cohort -> matches(rule.getEligibleCohorts(), cohort.name()))
                .orElse(false);
    }

    private boolean matches(List<String> eligibleCohorts, String cohortName) {
        return eligibleCohorts.stream()
                .filter(value -> value != null && !value.isBlank())
                .anyMatch(value -> value.trim().equalsIgnoreCase(cohortName));
    }

    @Override
    public String getStrategyType() {
        return "COHORT";
    }
}
