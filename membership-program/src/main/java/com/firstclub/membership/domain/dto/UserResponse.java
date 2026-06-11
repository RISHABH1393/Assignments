package com.firstclub.membership.domain.dto;

import com.firstclub.membership.domain.entity.User;
import com.firstclub.membership.domain.enums.UserCohort;

public record UserResponse(Long id, String name, String email, UserCohort cohort) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCohort());
    }
}
