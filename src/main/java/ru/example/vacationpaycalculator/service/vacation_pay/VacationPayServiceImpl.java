package ru.example.vacationpaycalculator.service.vacation_pay;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.example.vacationpaycalculator.dto.VacationPayPeriod;
import ru.example.vacationpaycalculator.dto.VacationPayResponse;
import ru.example.vacationpaycalculator.dto.VacationPaySpecifiedPeriod;
import ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation.PaidVacationDaysService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Validated
public class VacationPayServiceImpl implements VacationPayService {

    public static final BigDecimal AVG_WORKING_DAYS_MONTH = new BigDecimal("29.3");
    private final PaidVacationDaysService paidVacationDays;

    @Override
    public VacationPayResponse calculateVacationPay(@Valid VacationPayPeriod vacationPay) {
        return VacationPayResponse.of(
                calculateVacationPayForPeriod(vacationPay.getAvgSalary(), vacationPay.getVacationDurationInDays()),
                vacationPay.getVacationDurationInDays()
        );
    }

    @Override
    public VacationPayResponse calculateVacationPayIncludingSaturdayAndSunday(@Valid VacationPaySpecifiedPeriod vacationPay) {
        long daysVacation = paidVacationDays.getPaidVacationDaysIncludingSaturdayAndSunday(
                vacationPay.getVacationStart(),
                vacationPay.getVacationEnd()
        );
        return VacationPayResponse.of(
                calculateVacationPayForPeriod(vacationPay.getAvgSalary(), daysVacation),
                daysVacation);
    }

    @Override
    public VacationPayResponse calculateVacationPayWithoutSaturdayAndSunday(@Valid VacationPaySpecifiedPeriod vacationPay) {
        long daysVacation = paidVacationDays.getPaidVacationDaysWithoutSaturdayAndSunday(
                vacationPay.getVacationStart(),
                vacationPay.getVacationEnd()
        );
        return VacationPayResponse.of(
                calculateVacationPayForPeriod(vacationPay.getAvgSalary(), daysVacation),
                daysVacation);
    }

    private BigDecimal calculateVacationPayForPeriod(@NotNull BigDecimal avgSalary, @NotNull Long daysVacation) {
        return avgSalary.divide(AVG_WORKING_DAYS_MONTH, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(daysVacation))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
