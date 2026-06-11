package com.firstclub.membership.event;

import com.firstclub.membership.domain.enums.PlanType;
import com.firstclub.membership.domain.enums.TierName;
import org.springframework.context.ApplicationEvent;

public class MembershipEvent extends ApplicationEvent {

    public enum EventType {
        SUBSCRIBED,
        TIER_UPGRADED,
        TIER_DOWNGRADED,
        CANCELLED
    }

    private final Long userId;
    private final EventType eventType;
    private final TierName fromTier;
    private final TierName toTier;
    private final PlanType planType;

    public MembershipEvent(Object source, Long userId, EventType eventType, TierName fromTier, TierName toTier, PlanType planType) {
        super(source);
        this.userId = userId;
        this.eventType = eventType;
        this.fromTier = fromTier;
        this.toTier = toTier;
        this.planType = planType;
    }

    public Long getUserId() {
        return userId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public TierName getFromTier() {
        return fromTier;
    }

    public TierName getToTier() {
        return toTier;
    }

    public PlanType getPlanType() {
        return planType;
    }
}
