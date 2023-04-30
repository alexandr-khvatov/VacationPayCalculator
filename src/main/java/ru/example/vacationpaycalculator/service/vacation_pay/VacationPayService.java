package ru.example.vacationpaycalculator.service.vacation_pay;

import ru.example.vacationpaycalculator.dto.VacationPayPeriod;
import ru.example.vacationpaycalculator.dto.VacationPayResponse;
import ru.example.vacationpaycalculator.dto.VacationPaySpecifiedPeriod;

import javax.validation.Valid;

public interface VacationPayService {
    VacationPayResponse calculateVacationPay(@Valid VacationPayPeriod vacationPay);

    VacationPayResponse calculateVacationPayIncludingSaturdayAndSunday(@Valid VacationPaySpecifiedPeriod vacationPay);

    VacationPayResponse calculateVacationPayWithoutSaturdayAndSunday(@Valid VacationPaySpecifiedPeriod vacationPay);
}
