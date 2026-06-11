package com.firstclub.membership.service;

import com.firstclub.membership.domain.dto.TierResponse;
import com.firstclub.membership.domain.enums.TierName;

import java.util.List;

public interface BenefitService {
    List<TierResponse.BenefitInfo> getBenefitsForTier(TierName tierName);

    List<TierResponse.BenefitInfo> getUserActiveBenefits(Long userId);
}
