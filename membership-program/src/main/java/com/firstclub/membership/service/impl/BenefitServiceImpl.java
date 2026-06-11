package com.firstclub.membership.service.impl;

import com.firstclub.membership.benefit.BenefitProvider;
import com.firstclub.membership.domain.dto.TierResponse;
import com.firstclub.membership.domain.entity.UserMembership;
import com.firstclub.membership.domain.enums.TierName;
import com.firstclub.membership.repository.UserMembershipRepository;
import com.firstclub.membership.service.BenefitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BenefitServiceImpl implements BenefitService {

    private final BenefitProvider benefitProvider;
    private final UserMembershipRepository userMembershipRepository;

    public BenefitServiceImpl(BenefitProvider benefitProvider, UserMembershipRepository userMembershipRepository) {
        this.benefitProvider = benefitProvider;
        this.userMembershipRepository = userMembershipRepository;
    }

    @Override
    public List<TierResponse.BenefitInfo> getBenefitsForTier(TierName tierName) {
        return benefitProvider.getBenefits(tierName).stream()
                .map(benefit -> new TierResponse.BenefitInfo(benefit.getBenefitType(), benefit.getBenefitValue(), benefit.getDescription()))
                .toList();
    }

    @Override
    public List<TierResponse.BenefitInfo> getUserActiveBenefits(Long userId) {
        return userMembershipRepository.findActiveByUserId(userId)
                .map(UserMembership::getTier)
                .map(tier -> tier.getBenefits().stream()
                        .map(benefit -> new TierResponse.BenefitInfo(benefit.getBenefitType(), benefit.getBenefitValue(), benefit.getDescription()))
                        .toList())
                .orElse(List.of());
    }
}
