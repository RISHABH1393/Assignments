package com.firstclub.membership.domain.entity;

import com.firstclub.membership.domain.enums.PlanType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false, unique = true)
    private PlanType planType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private boolean active = true;

    protected MembershipPlan() {
    }

    public MembershipPlan(PlanType planType, BigDecimal price, int durationDays, String label, boolean active) {
        this.planType = planType;
        this.price = price;
        this.durationDays = durationDays;
        this.label = label;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public PlanType getPlanType() {
        return planType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public String getLabel() {
        return label;
    }

    public boolean isActive() {
        return active;
    }
}
