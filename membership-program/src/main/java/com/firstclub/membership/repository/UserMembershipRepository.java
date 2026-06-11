package com.firstclub.membership.repository;

import com.firstclub.membership.domain.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    @Query("SELECT m FROM UserMembership m WHERE m.user.id = :userId AND m.status = 'ACTIVE'")
    Optional<UserMembership> findActiveByUserId(@Param("userId") Long userId);
}
