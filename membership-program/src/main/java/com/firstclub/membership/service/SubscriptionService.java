package com.firstclub.membership.service;

import com.firstclub.membership.domain.dto.MembershipResponse;
import com.firstclub.membership.domain.dto.SubscribeRequest;

public interface SubscriptionService {
    MembershipResponse subscribe(SubscribeRequest request);

    MembershipResponse upgradeTier(Long userId);

    MembershipResponse downgradeTier(Long userId);

    void cancel(Long userId);

    MembershipResponse getMembership(Long userId);
}
