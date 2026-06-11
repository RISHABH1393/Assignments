package com.firstclub.membership.domain.dto;

import com.firstclub.membership.domain.enums.TierName;

public record EligibleTierResponse(Long userId, TierName eligibleTier, String message) {
}
