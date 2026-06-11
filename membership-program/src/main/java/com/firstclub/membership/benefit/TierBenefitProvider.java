package com.firstclub.membership.benefit;

import com.firstclub.membership.domain.entity.MembershipTier;
import com.firstclub.membership.domain.entity.TierBenefit;
import com.firstclub.membership.domain.enums.TierName;
import com.firstclub.membership.repository.MembershipTierRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TierBenefitProvider implements BenefitProvider {

    private final MembershipTierRepository membershipTierRepository;

    public TierBenefitProvider(MembershipTierRepository membershipTierRepository) {
        this.membershipTierRepository = membershipTierRepository;
    }

    @Override
    public List<TierBenefit> getBenefits(TierName tierName) {
        return membershipTierRepository.findByTierName(tierName)
                .map(MembershipTier::getBenefits)
                .orElse(Collections.emptyList());
    }
}
