package com.example.demo.services;

import com.example.demo.model.dtos.OsActiveDto;
import com.example.demo.model.dtos.OsDto;
import com.example.demo.model.dtos.TechnicalDto;
import com.example.demo.model.entitys.AccumulatedExpenseEntity;
import com.example.demo.model.entitys.OsEntity;
import com.example.demo.model.entitys.TechnicalEntity;
import com.example.demo.repository.OsRepository;
import com.example.demo.repository.TechnicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CalcServicePerma {

    @Autowired
    OsRepository osRepository;

    @Autowired
    TechnicalRepository technicalRepository;

    @Autowired
    CsvReaderService csvReaderService;

    @Autowired
    CalcDistanceService calcDistanceService;

    @Autowired
    CalcAmountTemp calcAmountTemp;

    public OsDto makeOs(OsDto osDto){

        if(osRepository.existsByOsNumber(osDto.osNumber())){
            throw new RuntimeException("This Os have exists!");
        }
        OsEntity osEntity= new OsEntity(osDto);

        osRepository.save(osEntity);
        return osDto;
    }
    public List<OsDto> makeOsByCsvList(MultipartFile filePath) {
        List<OsDto> osDtoList = csvReaderService.fileCsvReader(filePath);
        List<OsEntity> osEntitys = new ArrayList<>();

        for (int i = 0; i < osDtoList.size(); i++) {
            OsDto osTemp = osDtoList.get(i);
            OsEntity osEntity = new OsEntity();
            osEntity.setContract(osTemp.contract());
            osEntity.setOsNumber(osTemp.osNumber());
            osEntity.setOccurrence(osTemp.occurence());
            osEntity.setUnit(osTemp.unit());
            osEntity.setScreeningDate(osTemp.screeningDate());
            osEntity.setDistanceBaseOs(osTemp.distanceBaseOs());
            osEntity.setArea(osTemp.area());
            osEntity.setLatitude(osTemp.latitude());
            osEntity.setLongitude(osTemp.longitude());
            osEntity.setResponsibleScreening(osTemp.responsibleScreening());
            osEntitys.add(osEntity);
        }
        osRepository.saveAll(osEntitys);
        return osDtoList;
    }

    public OsDto getOsEntity(@RequestParam Integer os){
        Optional<OsEntity> osEntity = osRepository.findByOsNumber(os);

        if (osEntity.isEmpty()) {
            throw new RuntimeException("404 not found");
        }
        OsEntity osEntity1 = osEntity.get();

        return new OsDto(
                osEntity1.getId(),
                osEntity1.getContract(),
                osEntity1.getOsNumber(),
                osEntity1.getOccurrence(),
                osEntity1.getUnit(),
                osEntity1.getScreeningDate(),
                osEntity1.getDistanceBaseOs(),
                osEntity1.getArea(),
                osEntity1.getLatitude(),
                osEntity1.getLongitude(),
                osEntity1.getResponsibleScreening()
        );
    }

    public List<OsDto> getAllOs(){
        return osRepository.findAll().stream()
                .map(osEntity -> new OsDto(null,
                        osEntity.getContract(),
                        osEntity.getOsNumber(),
                        osEntity.getOccurrence(),
                        osEntity.getUnit(),
                        osEntity.getScreeningDate(),
                        osEntity.getDistanceBaseOs(),
                        osEntity.getArea(),
                        osEntity.getLatitude(),
                        osEntity.getLongitude(),
                        osEntity.getResponsibleScreening()
                ))
                .collect(Collectors.toList());
    }

    //This method does two things because it's part of the business rule that, when deactivated in this system, it accumulates the cost that would have been incurred to go there.
    public void updateOsInactive(Integer osNumber){

            OsEntity osEntity = osRepository.findByOsNumber(osNumber)
                .orElseThrow(() -> new RuntimeException("This os doesn't exist in database."));
                OsActiveDto osActiveDto=convertEntityToDto(osEntity);


                osEntity.setEnable(false);
                String amount=calcDistanceService.expensiveAvoided(osActiveDto.osDto(), osActiveDto.technicalDto());
                String cleanFormat=amount.replace("R$", "").trim();
                cleanFormat=cleanFormat.replace(".", "").replace(",", ".");

                Double convertedAmount=Double.parseDouble(cleanFormat);

                AccumulatedExpenseEntity accumulatedExpenseEntity=new AccumulatedExpenseEntity(convertedAmount);

                osRepository.save(osEntity);

    }

    public OsActiveDto convertEntityToDto(OsEntity osEntity) {
        OsDto osDto = new OsDto(
                osEntity.getId(),
                osEntity.getContract(),
                osEntity.getOsNumber(),
                osEntity.getOccurrence(),
                osEntity.getUnit(),
                osEntity.getScreeningDate(),
                osEntity.getDistanceBaseOs(),
                osEntity.getArea(),
                osEntity.getLatitude(),
                osEntity.getLongitude(),
                osEntity.getResponsibleScreening()
        );

        TechnicalDto technicalDto = Optional.ofNullable(osEntity.getTechnical())
                .map(technical -> new TechnicalDto(osEntity.getTechnical().getId(),
                        osEntity.getTechnical().getName(),
                        osEntity.getTechnical().getOsNumber(),
                        osEntity.getTechnical().getContract(),
                        osEntity.getTechnical().getLatitude(),
                        osEntity.getTechnical().getLongitude(),
                        osEntity.getTechnical().getCar(),
                        osEntity.getTechnical().getKmXLCar()

                ))
                .orElse(null);

        return new OsActiveDto(osDto, osEntity.getIsEnable(), technicalDto);
    }

    public String osAvoidedSpent(OsActiveDto osActiveDto){

         return calcDistanceService.expensiveAvoided(osActiveDto.osDto(),osActiveDto.technicalDto());
    }

    public String osDistanceByGeoLocation(Double latitude1,Double longitude1,Double latitude2,Double longitude2){
        Double distance=calcDistanceService.distanceCalculatedByGeoLocation(latitude1,longitude1,latitude2,longitude2);
        return distance + "km";
    }

    public TechnicalDto saveTechnical(TechnicalDto technicalDto){

        if (technicalDto.id() != null) {
            if (technicalRepository.existsById(technicalDto.id())) {
                throw new RuntimeException("This technical already exists");
            }
        }

        TechnicalEntity technicalEntity= new TechnicalEntity(technicalDto);

        technicalEntity.setId(null);

        technicalRepository.save(technicalEntity);

        return new TechnicalDto(technicalEntity.getId(),
                technicalEntity.getName(),
                technicalEntity.getOsNumber(),
                technicalEntity.getContract(),
                technicalEntity.getLatitude(),
                technicalEntity.getLongitude(),
                technicalEntity.getCar(),
                technicalEntity.getKmXLCar());
    }

    public String amountPlus(){
        return "R$" + calcAmountTemp.getAllAmountTemp();
    }

    public List<OsActiveDto> getActiveOsEntities() {
        List<OsEntity> allOs = osRepository.findAll();
        List<OsActiveDto> activeOs = new ArrayList<>();
        for (OsEntity os : allOs) {
            if (os.getIsEnable()== true) {
                activeOs.add(new OsActiveDto(new OsDto(
                        os.getId(),
                        os.getContract(),
                        os.getOsNumber(),
                        os.getOccurrence(),
                        os.getUnit(),
                        os.getScreeningDate(),
                        os.getDistanceBaseOs(),
                        os.getArea(),
                        os.getLatitude(),
                        os.getLongitude(),
                        os.getResponsibleScreening()
                ),os.getIsEnable(),null));
            }
        }
        return activeOs;
    }

}
