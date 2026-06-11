package com.firstclub.membership.controller;

import com.firstclub.membership.domain.dto.PlanResponse;
import com.firstclub.membership.repository.MembershipPlanRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final MembershipPlanRepository membershipPlanRepository;

    public PlanController(MembershipPlanRepository membershipPlanRepository) {
        this.membershipPlanRepository = membershipPlanRepository;
    }

    @GetMapping
    public List<PlanResponse> getPlans() {
        return membershipPlanRepository.findAllByActiveTrue().stream().map(PlanResponse::from).toList();
    }
}
