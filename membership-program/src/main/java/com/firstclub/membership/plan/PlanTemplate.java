package com.firstclub.membership.plan;

import com.firstclub.membership.domain.enums.PlanType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public abstract class PlanTemplate {

    public abstract PlanType getPlanType();

    public abstract BigDecimal getBasePrice();

    public abstract int getDurationDays();

    public abstract String getLabel();

    protected BigDecimal getDiscountPercent() {
        return BigDecimal.ZERO;
    }

    public final BigDecimal getEffectivePrice() {
        BigDecimal discount = getDiscountPercent().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return getBasePrice().multiply(BigDecimal.ONE.subtract(discount)).setScale(2, RoundingMode.HALF_UP);
    }

    public final LocalDate computeEndDate(LocalDate startDate) {
        return startDate.plusDays(getDurationDays());
    }
}
