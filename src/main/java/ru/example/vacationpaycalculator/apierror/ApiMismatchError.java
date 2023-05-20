package ru.example.vacationpaycalculator.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
class ApiMismatchError extends ApiSubError {
    private String field;
    private Object rejectedValue;
}
