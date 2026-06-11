package com.firstclub.membership.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MembershipEventListener {

    private static final Logger log = LoggerFactory.getLogger(MembershipEventListener.class);

    @EventListener
    public void handleMembershipEvent(MembershipEvent event) {
        String message = switch (event.getEventType()) {
            case SUBSCRIBED -> "User %s subscribed to %s".formatted(event.getUserId(), event.getPlanType());
            case TIER_UPGRADED -> "User %s upgraded from %s to %s".formatted(event.getUserId(), event.getFromTier(), event.getToTier());
            case TIER_DOWNGRADED -> "User %s downgraded from %s to %s".formatted(event.getUserId(), event.getFromTier(), event.getToTier());
            case CANCELLED -> "User %s membership cancelled".formatted(event.getUserId());
        };
        log.info(message);
    }
}
