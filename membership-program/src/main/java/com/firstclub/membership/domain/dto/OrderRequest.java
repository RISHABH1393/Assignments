package com.firstclub.membership.domain.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(
        @NotNull Long userId,
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal amount
) {
}
