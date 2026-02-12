package com.example.demo.model.dtos;

import com.example.demo.model.entitys.TechnicalEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TechnicalDto (

        Integer id,
        @NotBlank(message = "Name cannot be null.")
        String name,
        @NotNull(message = "OS number cannot be null")
        Integer osNumber,
        @NotNull(message = "Contract number cannot be null")
        Integer contract,
        @NotNull(message = "This field needs some information(latitude).")
        Double latitude,
        @NotNull(message = "This field needs some information(longitude).")
        Double longitude,
        @NotBlank(message = "Car name cannot be blank.")
        String car,
        @NotNull(message = "This field needs some information(KmCarXL).")
        Double kmCarXL

) {

    public TechnicalDto(TechnicalEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getOsNumber(),
                entity.getContract(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getCar(),
                entity.getKmCarXL()
        );
    }
}