package ru.example.vacationpaycalculator.service.vacation_pay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.example.vacationpaycalculator.dto.VacationPayPeriod;
import ru.example.vacationpaycalculator.dto.VacationPayResponse;
import ru.example.vacationpaycalculator.dto.VacationPaySpecifiedPeriod;
import ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation.PaidVacationDaysServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class VacationPayServiceImplTest {
    @Mock
    private PaidVacationDaysServiceImpl paidVacationDays;

    @InjectMocks
    private VacationPayServiceImpl vacationPayService;

    @Test
    void calculateVacationPay() {
        var vacationPayPeriod = new VacationPayPeriod(new BigDecimal("50000"), 12L);

        var actual = vacationPayService.calculateVacationPay(vacationPayPeriod);

        var expected = VacationPayResponse.of(new BigDecimal("20477.76"), 12L);
        assertThat(actual).isNotNull();
        assertThat(actual.getVacationPay()).isEqualByComparingTo(expected.getVacationPay());
        assertThat(actual.getVacationDurationInDays()).isEqualTo(expected.getVacationDurationInDays());
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForPaidVacationDaysIncludingSaturdayAndSunday")
    void calculateVacationPayIncludingSaturdayAndSunday(BigDecimal avgSalary, LocalDate startVocation, LocalDate endVocation, long days, VacationPayResponse expected) {
        var vacationPayPeriod = new VacationPaySpecifiedPeriod(avgSalary, startVocation, endVocation);

        doReturn(days).when(paidVacationDays).getPaidVacationDaysIncludingSaturdayAndSunday(
                startVocation,
                endVocation
        );
        var actual = vacationPayService.calculateVacationPayIncludingSaturdayAndSunday(vacationPayPeriod);

        assertThat(actual).isNotNull();
        assertThat(actual.getVacationPay()).isEqualByComparingTo(expected.getVacationPay());
        assertThat(actual.getVacationDurationInDays()).isEqualTo(expected.getVacationDurationInDays());
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForPaidVacationDaysWithoutSaturdayAndSunday")
    void calculateVacationPayWithoutSaturdayAndSunday(BigDecimal avgSalary, LocalDate startVocation, LocalDate endVocation, long days, VacationPayResponse expected) {
        var vacationPayPeriod = new VacationPaySpecifiedPeriod(avgSalary, startVocation, endVocation);

        doReturn(days).when(paidVacationDays).getPaidVacationDaysWithoutSaturdayAndSunday(
                startVocation,
                endVocation
        );
        var actual = vacationPayService.calculateVacationPayWithoutSaturdayAndSunday(vacationPayPeriod);

        assertThat(actual).isNotNull();
        assertThat(actual.getVacationPay()).isEqualByComparingTo(expected.getVacationPay());
        assertThat(actual.getVacationDurationInDays()).isEqualTo(expected.getVacationDurationInDays());
    }

    static Stream<Arguments> getArgumentsForPaidVacationDaysIncludingSaturdayAndSunday() {
        return Stream.of(
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2022, 12, 27), LocalDate.of(2022, 12, 27), 1, VacationPayResponse.of(new BigDecimal("1706.48"), 1L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2023, 5, 9), LocalDate.of(2023, 5, 9), 0, VacationPayResponse.of(new BigDecimal("0"), 0L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2022, 12, 27), LocalDate.of(2023, 1, 11), 8, VacationPayResponse.of(new BigDecimal("13651.84"), 8L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 12), 13, VacationPayResponse.of(new BigDecimal("22184.24"), 13L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 23), 24, VacationPayResponse.of(new BigDecimal("40955.52"), 24L))
        );
    }

    static Stream<Arguments> getArgumentsForPaidVacationDaysWithoutSaturdayAndSunday() {
        return Stream.of(
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2022, 12, 27), LocalDate.of(2022, 12, 27), 1, VacationPayResponse.of(new BigDecimal("1706.48"), 1L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2023, 5, 9), LocalDate.of(2023, 5, 9), 0, VacationPayResponse.of(new BigDecimal("0"), 0L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2022, 12, 27), LocalDate.of(2023, 1, 11), 8, VacationPayResponse.of(new BigDecimal("13651.84"), 8L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 12), 13, VacationPayResponse.of(new BigDecimal("22184.24"), 13L)),
                Arguments.of(new BigDecimal("50000"), LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 23), 24, VacationPayResponse.of(new BigDecimal("40955.52"), 24L))
        );
    }
}
