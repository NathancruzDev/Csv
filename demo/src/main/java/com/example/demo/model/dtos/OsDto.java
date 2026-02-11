package com.example.demo.model.dtos;

import com.example.demo.model.UnitEnum;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record OsDto(

        Integer id,

        @NotNull(message = "Contract cannot be null")
        @OneToOne
        Integer contract,
        @OneToOne
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
        @Size(min=4,max=80)
        String responsibleScreening

) {


    @Override
    public String toString() {
        return String.format("""
        {
          "contract": "%s",
          "OsNumber": "%s",
          "Occurrence": "%s",
          "Unit": %s,
          "DistanceBaseToOs": %.6f,
          "Area": %s,
          "Latitude": %.6f,
          "Longitude": %.6f,
          "ResponsibleScreening": %s
        }
        """);
    }
}
