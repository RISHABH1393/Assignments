package com.firstclub.membership.domain.dto;

import com.firstclub.membership.domain.entity.MembershipTier;
import com.firstclub.membership.domain.entity.TierBenefit;
import com.firstclub.membership.domain.entity.UserMembership;
import com.firstclub.membership.domain.enums.BenefitType;
import com.firstclub.membership.domain.enums.MembershipStatus;
import com.firstclub.membership.domain.enums.PlanType;
import com.firstclub.membership.domain.enums.TierName;

import java.time.LocalDate;
import java.util.List;

public record MembershipResponse(
        Long membershipId,
        Long userId,
        String userName,
        PlanType planType,
        String planLabel,
        TierName tierName,
        int tierLevel,
        LocalDate startDate,
        LocalDate endDate,
        MembershipStatus status,
        List<BenefitInfo> benefits
) {
    public static MembershipResponse from(UserMembership membership) {
        MembershipTier tier = membership.getTier();
        return new MembershipResponse(
                membership.getId(),
                membership.getUser().getId(),
                membership.getUser().getName(),
                membership.getPlan().getPlanType(),
                membership.getPlan().getLabel(),
                tier.getTierName(),
                tier.getTierLevel(),
                membership.getStartDate(),
                membership.getEndDate(),
                membership.getStatus(),
                tier.getBenefits().stream().map(MembershipResponse::toBenefitInfo).toList()
        );
    }

    private static BenefitInfo toBenefitInfo(TierBenefit benefit) {
        return new BenefitInfo(benefit.getBenefitType(), benefit.getBenefitValue(), benefit.getDescription());
    }

    public record BenefitInfo(BenefitType benefitType, String value, String description) {
    }
}
