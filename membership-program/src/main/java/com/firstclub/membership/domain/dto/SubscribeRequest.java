package com.firstclub.membership.domain.dto;

import com.firstclub.membership.domain.enums.PlanType;
import com.firstclub.membership.domain.enums.TierName;
import jakarta.validation.constraints.NotNull;

public record SubscribeRequest(
        @NotNull Long userId,
        @NotNull PlanType planType,
        @NotNull TierName tierName
) {
}
