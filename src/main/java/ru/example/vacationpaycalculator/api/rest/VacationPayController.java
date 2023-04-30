package ru.example.vacationpaycalculator.api.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.example.vacationpaycalculator.dto.VacationPayPeriod;
import ru.example.vacationpaycalculator.dto.VacationPayResponse;
import ru.example.vacationpaycalculator.dto.VacationPaySpecifiedPeriod;
import ru.example.vacationpaycalculator.service.vacation_pay.VacationPayService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class VacationPayController {

    private final VacationPayService vacationPayService;

    @GetMapping("/calculate")
    public VacationPayResponse calculateVacationPay(VacationPayPeriod vacationPayPeriod) {
        return vacationPayService.calculateVacationPay(vacationPayPeriod);
    }

    @GetMapping("/calculateVacationPayIncludingSaturdayAndSunday")
    public VacationPayResponse calculateVacationPayIncludingSaturdayAndSunday(VacationPaySpecifiedPeriod vacationPayPeriod) {
        return vacationPayService.calculateVacationPayIncludingSaturdayAndSunday(vacationPayPeriod);
    }

    @GetMapping("/calculateVacationPayWithoutSaturdayAndSunday")
    public VacationPayResponse calculateVacationPayWithoutSaturdayAndSunday(VacationPaySpecifiedPeriod vacationPayPeriod) {
        return vacationPayService.calculateVacationPayWithoutSaturdayAndSunday(vacationPayPeriod);
    }
}
