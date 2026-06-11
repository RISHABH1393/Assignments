package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {
    long countByUser_Id(Long userId);

    @Query("SELECT SUM(o.amount) FROM UserOrder o WHERE o.user.id = :userId AND o.createdAt >= :since")
    Optional<BigDecimal> sumOrderValueSince(@Param("userId") Long userId, @Param("since") LocalDateTime since);
}
