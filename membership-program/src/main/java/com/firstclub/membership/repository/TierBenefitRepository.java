package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entity.TierBenefit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TierBenefitRepository extends JpaRepository<TierBenefit, Long> {
}
