package ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation;

import java.time.LocalDate;

public interface PaidVacationDaysService {

    long getPaidVacationDaysIncludingSaturdayAndSunday(LocalDate startVocation, LocalDate endVocation);
    long getPaidVacationDaysWithoutSaturdayAndSunday(LocalDate startVocation, LocalDate endVocation);

}
