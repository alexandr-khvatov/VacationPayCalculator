package ru.example.vacationpaycalculator.integration.api;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.vacationpaycalculator.integration.IntegrationTestBase;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
class VacationPayControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    public static String URL = "/api/v1";

    @Test
    void calculateVacationPay_shouldReturn200_whenSucceed() throws Exception {
        this.mockMvc.perform(get(URL + "/calculate")
                        .param("avgSalary", "50000")
                        .param("vacationDurationInDays", "12")
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.vacationPay").value("20477.76"),
                        jsonPath("$.vacationDurationInDays").value("12")
                );
    }

    @Test
    void calculateVacationPayIncludingSaturdayAndSunday_shouldReturn200_whenSucceed() throws Exception {
        this.mockMvc.perform(get(URL + "/calculateVacationPayIncludingSaturdayAndSunday")
                        .param("avgSalary", "50000")
                        .param("vacationStart", "2022-12-27")
                        .param("vacationEnd", "2023-01-19")
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.vacationPay").value("27303.68"),
                        jsonPath("$.vacationDurationInDays").value("16")
                );
    }

    @Test
    void calculateVacationPayIncludingSaturdayAndSunday_shouldReturn400_whenIncorrectSalary() throws Exception {
        this.mockMvc.perform(get(URL + "/calculateVacationPayIncludingSaturdayAndSunday")
                        .param("avgSalary", "-50000")
                        .param("vacationStart", "2022-12-27")
                        .param("vacationEnd", "2023-01-19")
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.message").value("Validation error")
                );
    }

    @Test
    void calculateVacationPayIncludingSaturdayAndSunday_shouldReturn400_whenIncorrectRangeDate() throws Exception {
        this.mockMvc.perform(get(URL + "/calculateVacationPayIncludingSaturdayAndSunday")
                        .param("avgSalary", "50000")
                        .param("vacationStart", "2022-12-27")
                        .param("vacationEnd", "2022-12-26")
                )
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.message").value("Wrong time range")
                );
    }

    @Test
    void calculateVacationPayWithoutSaturdayAndSunday_shouldReturn200_whenSucceed() throws Exception {
        this.mockMvc.perform(get(URL + "/calculateVacationPayWithoutSaturdayAndSunday")
                        .param("avgSalary", "50000")
                        .param("vacationStart", "2022-12-27")
                        .param("vacationEnd", "2023-01-19")
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.vacationPay").value("22184.24"),
                        jsonPath("$.vacationDurationInDays").value("13")
                );
    }
}