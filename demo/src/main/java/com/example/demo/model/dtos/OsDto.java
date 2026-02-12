package com.example.demo.model.dtos;

import com.example.demo.model.UnitEnum;
import com.example.demo.model.entitys.OsEntity;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record OsDto(
        Integer id,
        @NotNull(message = "Contract cannot be null")
        Integer contract,
        @NotNull(message = "OS number cannot be null")
        Integer osNumber,
        @NotBlank(message = "This field needs some information.")
        String occurence,
        @NotNull(message = "Unit must be specified")
        UnitEnum unit,
        @NotNull(message = "Screening date is required")
        @PastOrPresent(message = "Screening date must be in the present or past")
        LocalDate screeningDate,
        @NotNull
        Double distanceBaseOs,
        @NotBlank
        String area,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude,
        @NotBlank
        @Size(min = 4, max = 80)
        String responsibleScreening
) {

    public OsDto(OsEntity entity) {
        this(
                entity.getId(),
                entity.getContract(),
                entity.getOsNumber(),
                entity.getOccurrence(),
                entity.getUnit(),
                entity.getScreeningDate(),
                entity.getDistanceBaseOs(),
                entity.getArea(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getResponsibleScreening()
        );
    }

    @Override
    public String toString() {
        return String.format("""
        {
          "contract": "%s",
          "OsNumber": "%s",
          "Occurrence": "%s",
          "Unit": "%s",
          "DistanceBaseToOs": %.6f,
          "Area": "%s",
          "Latitude": %.6f,
          "Longitude": %.6f,
          "ResponsibleScreening": "%s"
        }
        """, contract, osNumber, occurence, unit, distanceBaseOs, area, latitude, longitude, responsibleScreening);
    }
}