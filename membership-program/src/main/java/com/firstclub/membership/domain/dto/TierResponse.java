package com.firstclub.membership.domain.dto;

import com.firstclub.membership.domain.entity.MembershipTier;
import com.firstclub.membership.domain.entity.TierBenefit;
import com.firstclub.membership.domain.enums.BenefitType;
import com.firstclub.membership.domain.enums.TierName;

import java.util.List;

public record TierResponse(
        Long id,
        TierName tierName,
        int tierLevel,
        List<BenefitInfo> benefits
) {
    public static TierResponse from(MembershipTier tier) {
        return new TierResponse(
                tier.getId(),
                tier.getTierName(),
                tier.getTierLevel(),
                tier.getBenefits().stream().map(TierResponse::toBenefitInfo).toList()
        );
    }

    private static BenefitInfo toBenefitInfo(TierBenefit benefit) {
        return new BenefitInfo(benefit.getBenefitType(), benefit.getBenefitValue(), benefit.getDescription());
    }

    public record BenefitInfo(BenefitType benefitType, String value, String description) {
    }
}
