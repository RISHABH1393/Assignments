package com.firstclub.membership.plan;

import com.firstclub.membership.domain.enums.PlanType;

import java.math.BigDecimal;

public class YearlyPlanTemplate extends PlanTemplate {
    @Override
    public PlanType getPlanType() {
        return PlanType.YEARLY;
    }

    @Override
    public BigDecimal getBasePrice() {
        return BigDecimal.valueOf(2499);
    }

    @Override
    public int getDurationDays() {
        return 365;
    }

    @Override
    protected BigDecimal getDiscountPercent() {
        return BigDecimal.valueOf(30);
    }

    @Override
    public String getLabel() {
        return "Yearly Plan - Rs.2499/year (save 30%)";
    }
}
