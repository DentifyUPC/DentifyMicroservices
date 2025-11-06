package com.upc.dentify.clinicmanagementservice.interfaces.rest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record UpdateShiftResource(@JsonFormat(pattern = "HH:mm") LocalTime startTime,
                                  @JsonFormat(pattern = "HH:mm") LocalTime endTime) {
}
