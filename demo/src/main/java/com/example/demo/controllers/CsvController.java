package com.example.demo.controllers;

import com.example.demo.model.dtos.OsActiveDto;
import com.example.demo.model.dtos.OsDto;
import com.example.demo.model.dtos.TechnicalDto;
import com.example.demo.model.entitys.OsEntity;
import com.example.demo.services.CentralService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.demo.model.dtos.JSON.GeolocationDistanceJSON.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CsvController {

    @Autowired
    CentralService centralService;

    @PostMapping("/orderService")
    @Transactional
    public ResponseEntity<OsDto> createOsCsv( @RequestBody OsDto osDto, UriComponentsBuilder uriComponentsBuilder){
        OsDto createdOs= centralService.makeOs(osDto);
        var uri= uriComponentsBuilder.path("api/upOsCsv/{osNumber}").buildAndExpand(createdOs.osNumber()).toUri();
        return ResponseEntity.created(uri).body(createdOs);
    }
    @PostMapping("/technicians")
    public ResponseEntity<TechnicalDto> postTechnical(@RequestParam TechnicalDto technicalDto, UriComponentsBuilder uriComponentsBuilder){
        TechnicalDto createdTechnical= centralService.saveTechnical(technicalDto);
        var uri = uriComponentsBuilder.path("api/createTechnical/{id}").buildAndExpand(createdTechnical.id()).toUri();
        return ResponseEntity.created(uri).body(createdTechnical);
    }
    @PostMapping("/upFile")
    @Transactional
    public ResponseEntity<List<OsDto>> postFile(@RequestParam MultipartFile file, UriComponentsBuilder uriComponentsBuilder){
        List<OsDto> createdList = centralService.makeOsByCsvList(file);
        var uri= uriComponentsBuilder.path("csvB2B/upFile").buildAndExpand(createdList).toUri();
        return ResponseEntity.created(uri).body(createdList);
    }

    @GetMapping("/getOrderServicer/{osNumber}")
    public ResponseEntity<OsDto> getOsByNumber(@PathVariable Integer os){
        OsDto osReturned= centralService.getOsEntity(os);
        return ResponseEntity.ok(osReturned);
    }

    @GetMapping("/getAllOrderService")
    public ResponseEntity<List<OsDto>> getAllOs(){
        List<OsDto> allOs= centralService.getAllOs();
        return ResponseEntity.ok(allOs);
    }

    @PostMapping("/getOrderServiceSpent")
    public  ResponseEntity<String> getOsSpent(@RequestParam Integer idTechnical, Integer osNumber ){
        String str= centralService.osAvoidedSpent(idTechnical, osNumber);
        return ResponseEntity.ok(str);
    }

    @GetMapping("/AllAmount")
    public ResponseEntity<String> getAllAmount(){
        String str= centralService.amountPlus();
        return ResponseEntity.ok(str);
    }
    @PostMapping("/GeoLocationDistance")
    public ResponseEntity<String> getDistanceByTwoPoints(@RequestBody GeolocationDTO geolocationDistance){
        String str= centralService.osDistanceByGeoLocation(geolocationDistance.latitude1(), geolocationDistance.longitude1(),
                geolocationDistance.latitude2(), geolocationDistance.longitude2());
        return ResponseEntity.ok(str);
    }

    @PutMapping("/{osNumber}")
    public ResponseEntity<OsActiveDto> inactiveOs(@RequestParam Integer osNUmber){
        centralService.updateOsInactive(osNUmber);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getAllTechnicals")
    public ResponseEntity<List<TechnicalDto>> allTechnicals(){
        List<TechnicalDto> listTechnical= centralService.allTechnicals();
        return ResponseEntity.ok(listTechnical);
    }

    @PutMapping("/SetTechincalToOs")
    @Transactional
    public ResponseEntity<String> setTechincalToOs(@RequestParam Integer id,@RequestParam Integer os){
        centralService.updatedSetTechnicalToOs(id,os);
        return ResponseEntity.ok("sucess");
    }

    @GetMapping("/GetDistanceByTechnicalToOs")
    public ResponseEntity<String> getDistanceByTechnicalToOs(@RequestParam Integer idTec, @RequestParam Integer os){
        String calculated= centralService.getDistanceTechnicalToOs(idTec, os);
        return ResponseEntity.ok(calculated);
    }
    @GetMapping({"/getAllOsActives"})
    public ResponseEntity<Optional<List<OsEntity>>> getAllOsActives(){
        Optional<List<OsEntity>> list= centralService.getOsEnable();
        return ResponseEntity.ok(list);
    }
}
