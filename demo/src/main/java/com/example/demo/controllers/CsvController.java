package com.example.demo.controllers;

import com.example.demo.model.dtos.OsActiveDto;
import com.example.demo.model.dtos.OsDto;
import com.example.demo.model.dtos.TechnicalDto;
import com.example.demo.repository.OsRepository;
import com.example.demo.services.CalcServicePerma;
import com.example.demo.services.CsvReaderService;
import jakarta.transaction.Transactional;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.demo.model.dtos.JSON.GeolocationDistanceJSON.*;

import java.util.List;



@RestController
@RequestMapping("csvB2B")
public class CsvController {

    @Autowired
    OsRepository osInterface;

    @Autowired
    CsvReaderService csvReaderService;

    @Autowired
    CalcServicePerma calcServicePerma;

    @PostMapping("/upOsCsv")
    @Transactional
    public ResponseEntity<OsDto> postOsCsv( @RequestBody OsDto osDto, UriComponentsBuilder uriComponentsBuilder){
        OsDto createdOs=calcServicePerma.makeOs(osDto);
        var uri= uriComponentsBuilder.path("csvB2B/upOsCsv/{osNumber}").buildAndExpand(createdOs.osNumber()).toUri();
        return ResponseEntity.created(uri).body(createdOs);
    }
    @PostMapping("/createTechnical")
    public ResponseEntity<TechnicalDto> postTechnical(@RequestParam TechnicalDto technicalDto, UriComponentsBuilder uriComponentsBuilder){
        TechnicalDto createdTechnical=calcServicePerma.saveTechnical(technicalDto);
        var uri = uriComponentsBuilder.path("csvB2B/createTechnical/{id}").buildAndExpand(createdTechnical.id()).toUri();
        return ResponseEntity.created(uri).body(createdTechnical);
    }
    @PostMapping("/upFile")
    @Transactional
    public ResponseEntity<List<OsDto>> postFile(@RequestParam MultipartFile file, UriComponentsBuilder uriComponentsBuilder){
        List<OsDto> createdList = calcServicePerma.makeOsByCsvList(file);
        var uri= uriComponentsBuilder.path("csvB2B/upFile").buildAndExpand(createdList).toUri();
        return ResponseEntity.created(uri).body(createdList);
    }

    @GetMapping("/getOsCSV")
    public ResponseEntity<OsDto> getOsCsv(@RequestParam Integer os){
        OsDto osReturned=calcServicePerma.getOsEntity(os);
        return ResponseEntity.ok(osReturned);
    }

    @GetMapping("/getAllOs")
    public ResponseEntity<List<OsDto>> getAllOs(){
        List<OsDto> allOs=calcServicePerma.getAllOs();
        return ResponseEntity.ok(allOs);
    }

    @PostMapping("/getOsSpent")
    public  ResponseEntity<String> getOsSpent(@RequestParam Integer idTechnical, Integer osNumber ){
        String str=calcServicePerma.osAvoidedSpent(idTechnical, osNumber);
        return ResponseEntity.ok(str);
    }

    @GetMapping("/AllAmount")
    public ResponseEntity<String> getAllAmount(){
        String str=calcServicePerma.amountPlus();
        return ResponseEntity.ok(str);
    }
    @PostMapping("/GeoLocationDistance")
    public ResponseEntity<String> getDistanceByTwoPoints(@RequestBody GeolocationDTO geolocationDistance){
        String str=calcServicePerma.osDistanceByGeoLocation(geolocationDistance.latitude1(), geolocationDistance.longitude1(),
                geolocationDistance.latitude2(), geolocationDistance.longitude2());
        return ResponseEntity.ok(str);
    }

    @PutMapping("/{osNumber}")
    public ResponseEntity<OsActiveDto> inactiveOs(@RequestParam Integer osNUmber){
        calcServicePerma.updateOsInactive(osNUmber);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getAllTechnicals")
    public ResponseEntity<List<TechnicalDto>> allTechnicals(){
        List<TechnicalDto> listTechnical=calcServicePerma.allTechnicals();
        return ResponseEntity.ok(listTechnical);
    }

    @PutMapping("/SetTechincalToOs")
    @Transactional
    public ResponseEntity<String> setTechincalToOs(@RequestParam Integer id,@RequestParam Integer os){
        calcServicePerma.updatedSetTechnicalToOs(id,os);
        return ResponseEntity.ok("sucess");
    }

    @GetMapping("/GetDistanceByTechnicalToOs")
    public ResponseEntity<String> getDistanceByTechnicalToOs(@RequestParam Integer idTec, @RequestParam Integer os){
        String calculated=calcServicePerma.getDistanceTechnicalToOs(idTec, os);
        return ResponseEntity.ok(calculated);
    }
}
