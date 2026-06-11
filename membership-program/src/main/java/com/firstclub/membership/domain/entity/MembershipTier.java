package com.firstclub.membership.domain.entity;

import com.firstclub.membership.domain.enums.TierName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membership_tiers")
public class MembershipTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier_name", nullable = false, unique = true)
    private TierName tierName;

    @Column(name = "tier_level", nullable = false, unique = true)
    private int tierLevel;

    @OneToMany(mappedBy = "tier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TierBenefit> benefits = new ArrayList<>();

    protected MembershipTier() {
    }

    public MembershipTier(TierName tierName, int tierLevel) {
        this.tierName = tierName;
        this.tierLevel = tierLevel;
    }

    public Long getId() {
        return id;
    }

    public TierName getTierName() {
        return tierName;
    }

    public int getTierLevel() {
        return tierLevel;
    }

    public List<TierBenefit> getBenefits() {
        return benefits;
    }

    public void addBenefit(TierBenefit benefit) {
        benefit.setTier(this);
        this.benefits.add(benefit);
    }
}
