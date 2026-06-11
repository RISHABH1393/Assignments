package com.firstclub.membership.controller;

import com.firstclub.membership.domain.dto.TierResponse;
import com.firstclub.membership.repository.MembershipTierRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tiers")
public class TierController {

    private final MembershipTierRepository membershipTierRepository;

    public TierController(MembershipTierRepository membershipTierRepository) {
        this.membershipTierRepository = membershipTierRepository;
    }

    @GetMapping
    public List<TierResponse> getTiers() {
        return membershipTierRepository.findAll(Sort.by(Sort.Direction.ASC, "tierLevel"))
                .stream()
                .map(TierResponse::from)
                .toList();
    }
}
