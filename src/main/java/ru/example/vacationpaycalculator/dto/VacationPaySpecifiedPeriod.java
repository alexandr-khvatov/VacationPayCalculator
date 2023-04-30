package ru.example.vacationpaycalculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VacationPaySpecifiedPeriod {

    @Min(0)
    @NotNull
    private BigDecimal avgSalary;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate vacationStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate vacationEnd;

}
