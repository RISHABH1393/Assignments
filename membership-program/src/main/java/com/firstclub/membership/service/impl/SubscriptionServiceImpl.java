package com.firstclub.membership.service.impl;

import com.firstclub.membership.domain.dto.MembershipResponse;
import com.firstclub.membership.domain.dto.SubscribeRequest;
import com.firstclub.membership.domain.entity.MembershipPlan;
import com.firstclub.membership.domain.entity.MembershipTier;
import com.firstclub.membership.domain.entity.User;
import com.firstclub.membership.domain.entity.UserMembership;
import com.firstclub.membership.domain.enums.MembershipStatus;
import com.firstclub.membership.domain.enums.PlanType;
import com.firstclub.membership.domain.enums.TierName;
import com.firstclub.membership.event.MembershipEvent;
import com.firstclub.membership.exception.MembershipException;
import com.firstclub.membership.plan.PlanFactory;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import com.firstclub.membership.repository.UserMembershipRepository;
import com.firstclub.membership.repository.UserRepository;
import com.firstclub.membership.service.SubscriptionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipTierRepository membershipTierRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final PlanFactory planFactory;
    private final ApplicationEventPublisher eventPublisher;

    public SubscriptionServiceImpl(UserRepository userRepository,
                                   MembershipPlanRepository membershipPlanRepository,
                                   MembershipTierRepository membershipTierRepository,
                                   UserMembershipRepository userMembershipRepository,
                                   PlanFactory planFactory,
                                   ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.membershipTierRepository = membershipTierRepository;
        this.userMembershipRepository = userMembershipRepository;
        this.planFactory = planFactory;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public MembershipResponse subscribe(SubscribeRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new MembershipException("User not found"));

        userMembershipRepository.findActiveByUserId(user.getId()).ifPresent(existing -> {
            throw new MembershipException("User already has an active membership");
        });

        MembershipPlan plan = membershipPlanRepository.findByPlanTypeAndActiveTrue(request.planType())
                .orElseThrow(() -> new MembershipException("Plan not available"));

        MembershipTier tier = membershipTierRepository.findByTierName(request.tierName())
                .orElseThrow(() -> new MembershipException("Tier not found"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = planFactory.create(request.planType()).computeEndDate(startDate);

        UserMembership membership = UserMembership.builder()
                .user(user)
                .plan(plan)
                .tier(tier)
                .startDate(startDate)
                .endDate(endDate)
                .status(MembershipStatus.ACTIVE)
                .build();

        UserMembership saved = userMembershipRepository.save(membership);
        eventPublisher.publishEvent(new MembershipEvent(this, user.getId(), MembershipEvent.EventType.SUBSCRIBED, null, tier.getTierName(), plan.getPlanType()));
        return MembershipResponse.from(saved);
    }

    @Override
    public MembershipResponse upgradeTier(Long userId) {
        UserMembership membership = getActiveMembership(userId);
        int nextLevel = membership.getTier().getTierLevel() + 1;
        MembershipTier nextTier = membershipTierRepository.findByTierLevel(nextLevel)
                .orElseThrow(() -> new MembershipException("User is already at the highest tier"));

        TierName fromTier = membership.getTier().getTierName();
        membership.setTier(nextTier);
        UserMembership saved = userMembershipRepository.save(membership);
        eventPublisher.publishEvent(new MembershipEvent(this, userId, MembershipEvent.EventType.TIER_UPGRADED, fromTier, nextTier.getTierName(), saved.getPlan().getPlanType()));
        return MembershipResponse.from(saved);
    }

    @Override
    public MembershipResponse downgradeTier(Long userId) {
        UserMembership membership = getActiveMembership(userId);
        int previousLevel = membership.getTier().getTierLevel() - 1;
        MembershipTier previousTier = membershipTierRepository.findByTierLevel(previousLevel)
                .orElseThrow(() -> new MembershipException("User is already at the lowest tier"));

        TierName fromTier = membership.getTier().getTierName();
        membership.setTier(previousTier);
        UserMembership saved = userMembershipRepository.save(membership);
        eventPublisher.publishEvent(new MembershipEvent(this, userId, MembershipEvent.EventType.TIER_DOWNGRADED, fromTier, previousTier.getTierName(), saved.getPlan().getPlanType()));
        return MembershipResponse.from(saved);
    }

    @Override
    public void cancel(Long userId) {
        UserMembership membership = getActiveMembership(userId);
        membership.setStatus(MembershipStatus.CANCELLED);
        UserMembership saved = userMembershipRepository.save(membership);
        eventPublisher.publishEvent(new MembershipEvent(this, userId, MembershipEvent.EventType.CANCELLED, saved.getTier().getTierName(), null, saved.getPlan().getPlanType()));
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipResponse getMembership(Long userId) {
        return MembershipResponse.from(getActiveMembership(userId));
    }

    private UserMembership getActiveMembership(Long userId) {
        return userMembershipRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new MembershipException("No active membership found for user"));
    }
}
