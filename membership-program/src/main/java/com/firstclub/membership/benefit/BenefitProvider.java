package com.firstclub.membership.benefit;

import com.firstclub.membership.domain.entity.TierBenefit;
import com.firstclub.membership.domain.enums.TierName;

import java.util.List;

public interface BenefitProvider {
    List<TierBenefit> getBenefits(TierName tierName);
}
