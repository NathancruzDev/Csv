package com.example.demo.services;

import com.example.demo.client.GeoapifyClient;
import com.example.demo.model.dtos.JSON.GeoApifyJson.DistanceResponseDto;
import com.example.demo.model.dtos.OsDto;
import com.example.demo.model.dtos.TechnicalDto;
import com.example.demo.model.entitys.OsEntity;
import com.example.demo.model.entitys.TechnicalEntity;
import com.example.demo.repository.OsRepository;
import com.example.demo.repository.TechnicalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

/*
 *https://myprojects.geoapify.com/projects
 *
 *  */
@Service
public class CalcDistanceService extends GeoapifyClient {
    @Autowired
    OsRepository osRepository;

    @Autowired
    TechnicalRepository technicalRepository;

    private final Double fuel=6.22;

    GeoapifyClient geoapifyClient=new GeoapifyClient();

    public DistanceResponseDto distancexKm(TechnicalDto technicalDto, OsDto osDto){
        try {
            Double distanceCalculated = distanceCalculatedByGeoLocation(
                    technicalDto.latitude(),
                    technicalDto.longitude(),
                    osDto.latitude(),
                    osDto.longitude()
            );

            return new DistanceResponseDto(distanceCalculated);
        } catch (Exception e) {
            throw new RuntimeException("Erro no cálculo: " + e.getMessage());
        }
    }
   public String expensiveAvoided(OsDto osDto,TechnicalDto technicalDto){
       Double distance = distancexKm(technicalDto, osDto).distanceKm();

       Double calcLiteers = distance / technicalDto.kmCarXL();
       Double totalCust = calcLiteers * fuel;

       NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
       return nf.format(totalCust);
    }

    public Double distanceCalculatedByGeoLocation(Double latitude1,Double longitude1,Double latitude2,Double longitude2){
        try{
            String distanced= geoapifyClient.distance(latitude1,longitude1,latitude2,longitude2);
            double metersToKm = new ObjectMapper()
                    .readTree(distanced)
                    .path("features").get(0)
                    .path("properties")
                    .path("distance")
                    .asDouble();

            return metersToKm / 1000;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Double distanceTechinalToOs(Integer idTec, Integer os){
        try{
            Optional<OsEntity> osEntity= Optional.of(osRepository.findByOsNumber(os).orElseThrow(()->new RuntimeException("This os not exists.")));
            Optional<TechnicalEntity> technicalEntity= Optional.ofNullable(technicalRepository.findById(idTec).orElseThrow(() -> new RuntimeException("This technical not exists")));
            String distanced=geoapifyClient.distance(technicalEntity.get().getLatitude(), technicalEntity.get().getLongitude(), osEntity.get().getLatitude(), osEntity.get().getLongitude());
            return Double.parseDouble(distanced)/1000;
        }catch (Exception e){
            throw  new RuntimeException("Error");
        }
    }

}
