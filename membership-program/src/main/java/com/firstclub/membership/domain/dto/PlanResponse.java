package com.firstclub.membership.domain.dto;

import com.firstclub.membership.domain.entity.MembershipPlan;
import com.firstclub.membership.domain.enums.PlanType;

import java.math.BigDecimal;

public record PlanResponse(
        Long id,
        PlanType planType,
        String label,
        BigDecimal price,
        int durationDays
) {
    public static PlanResponse from(MembershipPlan plan) {
        return new PlanResponse(plan.getId(), plan.getPlanType(), plan.getLabel(), plan.getPrice(), plan.getDurationDays());
    }
}
