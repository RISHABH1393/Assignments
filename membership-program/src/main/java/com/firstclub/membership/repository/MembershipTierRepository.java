package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entity.MembershipTier;
import com.firstclub.membership.domain.enums.TierName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipTierRepository extends JpaRepository<MembershipTier, Long> {
    Optional<MembershipTier> findByTierName(TierName tierName);

    Optional<MembershipTier> findByTierLevel(int tierLevel);
}
