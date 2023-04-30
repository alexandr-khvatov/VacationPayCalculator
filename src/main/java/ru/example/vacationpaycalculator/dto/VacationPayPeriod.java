package ru.example.vacationpaycalculator.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class VacationPayPeriod {

    @Min(0)
    @NotNull
    private BigDecimal avgSalary;

    @Min(0)
    @NotNull
    private Long vacationDurationInDays;

}
