package com.firstclub.membership.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "tier.criteria")
public class TierCriteriaProperties {

    private Map<String, TierRule> rules = new HashMap<>();

    public Map<String, TierRule> getRules() {
        return rules;
    }

    public void setRules(Map<String, TierRule> rules) {
        this.rules = rules;
    }

    public TierRule getRuleForTier(String tierName) {
        if (tierName == null) {
            return null;
        }
        return rules.get(tierName.toLowerCase(Locale.ROOT));
    }

    public static class TierRule {
        private int minOrderCount = 0;
        private BigDecimal minOrderValue = BigDecimal.ZERO;
        private List<String> eligibleCohorts = new ArrayList<>();

        public int getMinOrderCount() {
            return minOrderCount;
        }

        public void setMinOrderCount(int minOrderCount) {
            this.minOrderCount = minOrderCount;
        }

        public BigDecimal getMinOrderValue() {
            return minOrderValue;
        }

        public void setMinOrderValue(BigDecimal minOrderValue) {
            this.minOrderValue = minOrderValue == null ? BigDecimal.ZERO : minOrderValue;
        }

        public List<String> getEligibleCohorts() {
            return eligibleCohorts;
        }

        public void setEligibleCohorts(List<String> eligibleCohorts) {
            this.eligibleCohorts = eligibleCohorts == null ? new ArrayList<>() : eligibleCohorts;
        }
    }
}
