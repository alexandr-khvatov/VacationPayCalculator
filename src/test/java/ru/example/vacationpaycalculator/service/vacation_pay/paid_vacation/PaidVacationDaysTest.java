package ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import ru.example.vacationpaycalculator.config.HolidayDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PaidVacationDaysTest {
    private PaidVacationDaysServiceImpl paidVacationDays;

    @Value("classpath:static/holidays_ru_test.json")
    Resource holidaysResourceFile;

    @BeforeEach
    void init() throws IOException {
        var stubOfYear = 2024;
        var file = holidaysResourceFile.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        List<HolidayDto> holidayDtos = mapper.readValue(file, new TypeReference<>() {
        });
        paidVacationDays = new PaidVacationDaysServiceImpl(
                holidayDtos.stream().collect(
                        groupingBy(HolidayDto::getMonth,
                                mapping(HolidayDto::getDay, toSet())))
        );

    }

    @ParameterizedTest
    @MethodSource("getInvalidTimeRanges")
    void getPaidVacationDaysIncludingSaturdayAndSunday_shouldThrowsExc_whenInvalidTimeRanges(LocalDate startVocation, LocalDate endVocation) {
        assertThrows(IllegalArgumentException.class, () -> paidVacationDays.getPaidVacationDaysIncludingSaturdayAndSunday(startVocation, endVocation));
    }

    @ParameterizedTest
    @MethodSource("getInvalidTimeRanges")
    void getPaidVacationDaysWithoutSaturdayAndSunday_shouldThrowsExc_whenInvalidTimeRanges(LocalDate startVocation, LocalDate endVocation) {
        assertThrows(IllegalArgumentException.class, () -> paidVacationDays.getPaidVacationDaysWithoutSaturdayAndSunday(startVocation, endVocation));
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForPaidVacationDaysIncludingSaturdayAndSunday")
    void getPaidVacationDaysIncludingSaturdayAndSunday_shouldReturnPaidVacationDays_whenSucceed(LocalDate startVocation, LocalDate endVocation, long expected) {
        var actual = paidVacationDays.getPaidVacationDaysIncludingSaturdayAndSunday(startVocation, endVocation);
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForPaidVacationDaysWithoutSaturdayAndSunday")
    void getPaidVacationDaysWithoutSaturdayAndSunday_shouldReturnPaidVacationDays_whenSucceed(LocalDate startVocation, LocalDate endVocation, long expected) {
        var actual = paidVacationDays.getPaidVacationDaysWithoutSaturdayAndSunday(startVocation, endVocation);
        assertThat(actual).isEqualTo(expected);
    }


    static Stream<Arguments> getInvalidTimeRanges() {
        return Stream.of(
                Arguments.of(LocalDate.of(2023, 1, 1), LocalDate.of(2022, 1, 1)),
                Arguments.of(LocalDate.of(2023, 2, 1), LocalDate.of(2023, 1, 1))
        );
    }

    static Stream<Arguments> getArgumentsForPaidVacationDaysIncludingSaturdayAndSunday() {
        return Stream.of(
                Arguments.of(LocalDate.of(2022, 12, 27), LocalDate.of(2022, 12, 27), 1L),
                Arguments.of(LocalDate.of(2023, 5, 9), LocalDate.of(2023, 5, 9), 0L),
                Arguments.of(LocalDate.of(2022, 12, 27), LocalDate.of(2023, 1, 11), 8L),
                Arguments.of(LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 12), 13L),
                Arguments.of(LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 23), 24L)
        );
    }

    static Stream<Arguments> getArgumentsForPaidVacationDaysWithoutSaturdayAndSunday() {
        return Stream.of(
                Arguments.of(LocalDate.of(2022, 12, 27), LocalDate.of(2022, 12, 27), 1L),
                Arguments.of(LocalDate.of(2023, 5, 9), LocalDate.of(2023, 5, 9), 0L),
                Arguments.of(LocalDate.of(2022, 12, 27), LocalDate.of(2023, 1, 11), 7L),
                Arguments.of(LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 12), 9L),
                Arguments.of(LocalDate.of(2023, 4, 28), LocalDate.of(2023, 5, 23), 16L)
        );
    }
}