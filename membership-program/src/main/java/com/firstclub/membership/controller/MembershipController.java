package com.firstclub.membership.controller;

import com.firstclub.membership.domain.dto.EligibleTierResponse;
import com.firstclub.membership.domain.dto.MembershipResponse;
import com.firstclub.membership.domain.dto.SubscribeRequest;
import com.firstclub.membership.domain.enums.TierName;
import com.firstclub.membership.service.SubscriptionService;
import com.firstclub.membership.service.TierEvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    private final SubscriptionService subscriptionService;
    private final TierEvaluationService tierEvaluationService;

    public MembershipController(SubscriptionService subscriptionService, TierEvaluationService tierEvaluationService) {
        this.subscriptionService = subscriptionService;
        this.tierEvaluationService = tierEvaluationService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<MembershipResponse> subscribe(@Valid @RequestBody SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.subscribe(request));
    }

    @GetMapping("/{userId}")
    public MembershipResponse getMembership(@PathVariable Long userId) {
        return subscriptionService.getMembership(userId);
    }

    @PutMapping("/{userId}/upgrade")
    public MembershipResponse upgrade(@PathVariable Long userId) {
        return subscriptionService.upgradeTier(userId);
    }

    @PutMapping("/{userId}/downgrade")
    public MembershipResponse downgrade(@PathVariable Long userId) {
        return subscriptionService.downgradeTier(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> cancel(@PathVariable Long userId) {
        subscriptionService.cancel(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/eligible-tier")
    public EligibleTierResponse eligibleTier(@PathVariable Long userId) {
        TierName eligibleTier = tierEvaluationService.evaluateEligibleTier(userId);
        return new EligibleTierResponse(userId, eligibleTier, "Eligible tier evaluated as " + eligibleTier);
    }
}
