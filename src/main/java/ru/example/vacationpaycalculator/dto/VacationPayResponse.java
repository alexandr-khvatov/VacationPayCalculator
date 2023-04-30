package ru.example.vacationpaycalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@AllArgsConstructor(staticName = "of")
public class VacationPayResponse {
    @Min(0)
    @NotNull
    BigDecimal vacationPay;

    @Min(0)
    @NotNull
    Long vacationDurationInDays;
}
