package com.firstclub.membership.plan;

import com.firstclub.membership.domain.enums.PlanType;
import org.springframework.stereotype.Component;

@Component
public class PlanFactory {
    public PlanTemplate create(PlanType planType) {
        return switch (planType) {
            case MONTHLY -> new MonthlyPlanTemplate();
            case QUARTERLY -> new QuarterlyPlanTemplate();
            case YEARLY -> new YearlyPlanTemplate();
        };
    }
}
