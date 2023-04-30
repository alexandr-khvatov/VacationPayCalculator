package ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation;

import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;


@RequiredArgsConstructor
public class PaidVacationDaysServiceImpl implements PaidVacationDaysService {

    public final Map<Integer, Set<Integer>> holidays;
    public final Set<DayOfWeek> weekends = Set.of(SATURDAY, SUNDAY);

    @Override
    public long getPaidVacationDaysWithoutSaturdayAndSunday(LocalDate startVocation, LocalDate endVocation) {
        checkValidDataDuration(startVocation, endVocation);
        return startVocation.datesUntil(endVocation.plusDays(1))
                .filter(day -> !weekends.contains(day.getDayOfWeek()))
                .filter(this::isNotHolidays)
                .count();
    }

    @Override
    public long getPaidVacationDaysIncludingSaturdayAndSunday(LocalDate startVocation, LocalDate endVocation) {
        checkValidDataDuration(startVocation, endVocation);
        return startVocation.datesUntil(endVocation.plusDays(1))
                .filter(this::isNotHolidays)
                .count();
    }

    private void checkValidDataDuration(LocalDate startVocation, LocalDate endVocation) {
        if (startVocation.compareTo(endVocation) > 0) {
            throw new IllegalArgumentException("Wrong time range");
        }
    }

    private boolean isNotHolidays(LocalDate day) {
        if (holidays.containsKey(day.getMonthValue())) {
            return !holidays.get(day.getMonthValue()).contains(day.getDayOfMonth());
        }
        return true;
    }
}
