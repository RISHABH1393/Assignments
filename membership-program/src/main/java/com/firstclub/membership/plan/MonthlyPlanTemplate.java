package com.firstclub.membership.plan;

import com.firstclub.membership.domain.enums.PlanType;

import java.math.BigDecimal;

public class MonthlyPlanTemplate extends PlanTemplate {
    @Override
    public PlanType getPlanType() {
        return PlanType.MONTHLY;
    }

    @Override
    public BigDecimal getBasePrice() {
        return BigDecimal.valueOf(299);
    }

    @Override
    public int getDurationDays() {
        return 30;
    }

    @Override
    public String getLabel() {
        return "Monthly Plan - Rs.299/month";
    }
}
