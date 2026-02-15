package com.example.demo.services;

import com.example.demo.model.dtos.OsActiveDto;
import com.example.demo.model.dtos.OsDto;
import com.example.demo.model.dtos.TechnicalDto;
import com.example.demo.model.entitys.AccumulatedExpenseEntity;
import com.example.demo.model.entitys.OsEntity;
import com.example.demo.model.entitys.TechnicalEntity;
import com.example.demo.repository.AccumulatedRepository;
import com.example.demo.repository.OsRepository;
import com.example.demo.repository.TechnicalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CentralService {
    @Autowired
    OsRepository osRepository;

    @Autowired
    TechnicalRepository technicalRepository;

    @Autowired
    AccumulatedRepository accumulatedRepository;

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
            boolean verifyOsExist=osRepository.existsByOsNumber(osTemp.osNumber());
                if(verifyOsExist == true){
                    System.out.println("Objet always exists in database.");
                }
                else {
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

    @Transactional
    public void updateOsInactive(Integer osNumber) {

        OsEntity osEntity = osRepository.findByOsNumber(osNumber)
                .orElseThrow(() -> new RuntimeException("OS " + osNumber + " não encontrada no banco."));

        OsActiveDto osActiveDto = makeEntityToActiveOs(osEntity);
        osEntity.setEnable(false);

            if (osActiveDto.technicalDto() == null) {

                osRepository.save(osEntity);
                return;
            }

        String amountRaw = calcDistanceService.expensiveAvoided(osActiveDto.osDto(), osActiveDto.technicalDto());

        String cleanFormat = amountRaw.replace("R$", "")
                .replaceAll("[\\s\\u00A0]+", "")
                .replace(".", "")
                .replace(",", ".");

        Double convertedAmount = Double.parseDouble(cleanFormat);

        AccumulatedExpenseEntity accumulatedExpense = new AccumulatedExpenseEntity(convertedAmount);

        accumulatedRepository.save(accumulatedExpense);
        osRepository.save(osEntity);
    }

    public OsActiveDto makeEntityToActiveOs(OsEntity osEntity) {
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
                .map(t -> new TechnicalDto(
                        t.getId(),
                        t.getName(),
                        t.getOsNumber(),
                        t.getContract(),
                        t.getLatitude(),
                        t.getLongitude(),
                        t.getCar(),
                        t.getKmXLCar()
                ))
                .orElse(null);

        return new OsActiveDto(osDto, osEntity.getIsEnable(), technicalDto);
    }

    public String osAvoidedSpent(Integer id, Integer osNumber){
         OsDto osDto=osRepository.findByOsNumber(osNumber).map(x ->new OsDto(x)).orElseThrow(() -> new RuntimeException());
         TechnicalDto technicalDto=technicalRepository.findById(id).map(x -> new TechnicalDto(x)).orElseThrow(() -> new RuntimeException());
         return calcDistanceService.expensiveAvoided(osDto,technicalDto);
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
        return "R$" + calcAmountTemp.getAllAccumulated();
    }

    @Transactional
    public void updatedSetTechnicalToOs(Integer technicalId, Integer osNumber) {
        TechnicalEntity technical = technicalRepository.findById(technicalId)
                .orElseThrow(() -> new RuntimeException("Técnico com ID " + technicalId + " não existe."));

        OsEntity os = osRepository.findByOsNumber(osNumber)
                .orElseThrow(() -> new RuntimeException("OS " + osNumber + " não encontrada no banco."));

        technical.setOsNumber(osNumber);
        os.setEnable(true);

        makeEntityToActiveOs(os);

        osRepository.save(os);
        technicalRepository.save(technical);
    }

    public List<TechnicalDto> allTechnicals(){
        return technicalRepository.findAll().stream().map( technicalEntity -> new TechnicalDto(
                technicalEntity.getId(),
                technicalEntity.getName(),
                technicalEntity.getOsNumber(),
                technicalEntity.getContract(),
                technicalEntity.getLatitude(),
                technicalEntity.getLongitude(),
                technicalEntity.getCar(),
                technicalEntity.getKmXLCar()
                )).collect(Collectors.toList());
    }

    public String getDistanceTechnicalToOs(Integer idTec, Integer os){
        Double distanceInKm=calcDistanceService.distanceTechinalToOs(idTec,os);
        return distanceInKm + "Km";
    }

    public Optional<List<OsEntity>> getOsEnable(){
        Optional<List<OsEntity>> osEnable=osRepository.findByIsEnable(true);
        return osEnable;
    }

}
