package ru.example.vacationpaycalculator.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation.PaidVacationDaysService;
import ru.example.vacationpaycalculator.service.vacation_pay.paid_vacation.PaidVacationDaysServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

@Slf4j
@Configuration
public class HolidayConfig {

    @Value("classpath:static/holidays_ru.json")
    Resource holidaysResourceFile;

    @Bean
    public PaidVacationDaysService getHolidays() throws IOException {
        return new PaidVacationDaysServiceImpl(loadHolidays());
    }

    public Map<Integer, Set<Integer>> loadHolidays() throws IOException {
        try {
            var file = holidaysResourceFile.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            List<HolidayDto> holidayDtos = mapper.readValue(file, new TypeReference<>() {
            });
            return holidayDtos.stream().collect(groupingBy(HolidayDto::getMonth, mapping(HolidayDto::getDay, toSet())));
        } catch (IOException exception) {
            log.error("Error load file with holidays path:{}", holidaysResourceFile.getFilename());
            throw exception;
        }
    }
}
