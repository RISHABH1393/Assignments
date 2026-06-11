package com.firstclub.membership.domain.entity;

import com.firstclub.membership.domain.enums.BenefitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tier_benefits")
public class TierBenefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private MembershipTier tier;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type", nullable = false)
    private BenefitType benefitType;

    @Column(name = "benefit_value", nullable = false)
    private String benefitValue;

    @Column(nullable = false)
    private String description;

    protected TierBenefit() {
    }

    public TierBenefit(BenefitType benefitType, String benefitValue, String description) {
        this.benefitType = benefitType;
        this.benefitValue = benefitValue;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public MembershipTier getTier() {
        return tier;
    }

    public void setTier(MembershipTier tier) {
        this.tier = tier;
    }

    public BenefitType getBenefitType() {
        return benefitType;
    }

    public String getBenefitValue() {
        return benefitValue;
    }

    public String getDescription() {
        return description;
    }
}
