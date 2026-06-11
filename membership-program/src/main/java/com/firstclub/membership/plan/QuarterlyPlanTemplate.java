package com.firstclub.membership.plan;

import com.firstclub.membership.domain.enums.PlanType;

import java.math.BigDecimal;

public class QuarterlyPlanTemplate extends PlanTemplate {
    @Override
    public PlanType getPlanType() {
        return PlanType.QUARTERLY;
    }

    @Override
    public BigDecimal getBasePrice() {
        return BigDecimal.valueOf(799);
    }

    @Override
    public int getDurationDays() {
        return 90;
    }

    @Override
    protected BigDecimal getDiscountPercent() {
        return BigDecimal.valueOf(10);
    }

    @Override
    public String getLabel() {
        return "Quarterly Plan - Rs.799/quarter (save 10%)";
    }
}
