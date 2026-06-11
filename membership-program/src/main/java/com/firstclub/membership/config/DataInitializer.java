package com.firstclub.membership.config;

import com.firstclub.membership.domain.entity.MembershipPlan;
import com.firstclub.membership.domain.entity.MembershipTier;
import com.firstclub.membership.domain.entity.TierBenefit;
import com.firstclub.membership.domain.entity.User;
import com.firstclub.membership.domain.entity.UserOrder;
import com.firstclub.membership.domain.enums.BenefitType;
import com.firstclub.membership.domain.enums.PlanType;
import com.firstclub.membership.domain.enums.TierName;
import com.firstclub.membership.domain.enums.UserCohort;
import com.firstclub.membership.plan.PlanFactory;
import com.firstclub.membership.plan.PlanTemplate;
import com.firstclub.membership.repository.MembershipPlanRepository;
import com.firstclub.membership.repository.MembershipTierRepository;
import com.firstclub.membership.repository.OrderRepository;
import com.firstclub.membership.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipTierRepository membershipTierRepository;
    private final OrderRepository orderRepository;
    private final PlanFactory planFactory;

    public DataInitializer(UserRepository userRepository,
                           MembershipPlanRepository membershipPlanRepository,
                           MembershipTierRepository membershipTierRepository,
                           OrderRepository orderRepository,
                           PlanFactory planFactory) {
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.membershipTierRepository = membershipTierRepository;
        this.orderRepository = orderRepository;
        this.planFactory = planFactory;
    }

    @Override
    public void run(String... args) {
        seedPlans();
        seedTiers();
        seedUsersAndOrders();
        log.info("Seed data initialized");
    }

    private void seedPlans() {
        for (PlanType planType : PlanType.values()) {
            PlanTemplate template = planFactory.create(planType);
            MembershipPlan plan = new MembershipPlan(
                    template.getPlanType(),
                    template.getEffectivePrice(),
                    template.getDurationDays(),
                    template.getLabel(),
                    true
            );
            membershipPlanRepository.save(plan);
        }
    }

    private void seedTiers() {
        MembershipTier silver = new MembershipTier(TierName.SILVER, 1);
        silver.addBenefit(new TierBenefit(BenefitType.FREE_DELIVERY, "true", "Free delivery on every order"));
        silver.addBenefit(new TierBenefit(BenefitType.DISCOUNT_PERCENT, "5", "5% off on eligible products"));

        MembershipTier gold = new MembershipTier(TierName.GOLD, 2);
        gold.addBenefit(new TierBenefit(BenefitType.FREE_DELIVERY, "true", "Free delivery on every order"));
        gold.addBenefit(new TierBenefit(BenefitType.DISCOUNT_PERCENT, "10", "10% off on eligible products"));
        gold.addBenefit(new TierBenefit(BenefitType.EARLY_ACCESS, "true", "Early access to new launches"));

        MembershipTier platinum = new MembershipTier(TierName.PLATINUM, 3);
        platinum.addBenefit(new TierBenefit(BenefitType.FREE_DELIVERY, "true", "Free delivery on every order"));
        platinum.addBenefit(new TierBenefit(BenefitType.DISCOUNT_PERCENT, "20", "20% off on eligible products"));
        platinum.addBenefit(new TierBenefit(BenefitType.EARLY_ACCESS, "true", "Early access to new launches"));
        platinum.addBenefit(new TierBenefit(BenefitType.PRIORITY_SUPPORT, "true", "Priority support"));
        platinum.addBenefit(new TierBenefit(BenefitType.EXCLUSIVE_COUPONS, "PLAT10", "Exclusive coupon code"));

        membershipTierRepository.saveAll(List.of(silver, gold, platinum));
    }

    private void seedUsersAndOrders() {
        User alice = userRepository.save(new User("Alice", "alice@firstclub.demo", UserCohort.STANDARD));
        User bob = userRepository.save(new User("Bob", "bob@firstclub.demo", UserCohort.PREMIUM));
        User carol = userRepository.save(new User("Carol", "carol@firstclub.demo", UserCohort.VIP));

        for (int i = 0; i < 6; i++) {
            orderRepository.save(new UserOrder(alice, BigDecimal.valueOf(300)));
        }
        for (int i = 0; i < 16; i++) {
            orderRepository.save(new UserOrder(bob, BigDecimal.valueOf(400)));
        }
        orderRepository.flush();
    }
}
