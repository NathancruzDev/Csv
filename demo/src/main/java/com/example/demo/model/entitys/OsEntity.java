package com.example.demo.model.entitys;

import com.example.demo.model.dtos.AccumulatedExpensiveDto;
import com.example.demo.model.dtos.OsActiveDto;
import com.example.demo.model.dtos.OsDto;
import com.example.demo.repository.CoordinateInterface;
import com.example.demo.model.UnitEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "OS_ENTITY")
public class OsEntity implements CoordinateInterface {

    private Integer contract;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer osNumber;
    private String occurrence;
    private UnitEnum unit;
    //Data da Triagem
    private LocalDate screeningDate;
    private Double distanceBaseOs;
    private String area;
    private Double latitude;
    private Double longitude;
    private String responsibleScreening;
    private boolean isEnable;

    @ManyToOne
    @JoinColumn(name = "technical_id")
    private TechnicalEntity technical;




    public OsEntity(Integer id,Integer contract, Integer osNumber, String occurrence, UnitEnum unit, LocalDate screeningDate,
                    Double distanceBaseOs, String area, Double latitude, Double longitude, String responsibleScreening,boolean isEnable,TechnicalEntity technical) {
        this.id=id;
        this.contract = contract;
        this.osNumber = osNumber;
        this.occurrence = occurrence;
        this.unit = unit;
        this.screeningDate = screeningDate;
        this.distanceBaseOs = distanceBaseOs;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.responsibleScreening = responsibleScreening;
        this.isEnable= isEnable;
        this.technical=technical;
    }

    public OsEntity() {
    }

    public OsEntity(OsDto osDto) {
        this.id=osDto.id();
        this.contract=osDto.contract();
        this.osNumber=osDto.osNumber();
        this.occurrence = osDto.occurence();
        this.unit = osDto.unit();
        this.screeningDate = osDto.screeningDate();
        this.distanceBaseOs = osDto.distanceBaseOs();
        this.area = osDto.area();
        this.latitude =  osDto.latitude();
        this.longitude = osDto.longitude();
        this.responsibleScreening = osDto.responsibleScreening();
    }
    public OsEntity(OsActiveDto osActiveDto) {
        this.id=osActiveDto.osDto().id();
        this.contract=osActiveDto.osDto().contract();
        this.osNumber=osActiveDto.osDto().osNumber();
        this.occurrence = osActiveDto.osDto().occurence();
        this.unit = osActiveDto.osDto().unit();
        this.screeningDate = osActiveDto.osDto().screeningDate();
        this.distanceBaseOs = osActiveDto.osDto().distanceBaseOs();
        this.area = osActiveDto.osDto().area();
        this.latitude =  osActiveDto.osDto().latitude();
        this.longitude = osActiveDto.osDto().longitude();
        this.responsibleScreening = osActiveDto.osDto().responsibleScreening();
        this.isEnable=osActiveDto.isEnable();
    }


    public OsEntity(Integer contract, Integer id, Integer osNumber, String occurrence, UnitEnum unit, LocalDate screeningDate, Double distanceBaseOs, String area, Double latitude, Double longitude, String responsibleScreening) {
        this.contract = contract;
        this.id = id;
        this.osNumber = osNumber;
        this.occurrence = occurrence;
        this.unit = unit;
        this.screeningDate = screeningDate;
        this.distanceBaseOs = distanceBaseOs;
        this.area = area;
        this.latitude = latitude;
        this.longitude = longitude;
        this.responsibleScreening = responsibleScreening;
    }

    public OsEntity(AccumulatedExpensiveDto accumulatedExpensiveDto) {
    }

    public Integer getId() {
        return id;
    }

    public Integer getContract() {
        return contract;
    }

    public void setContract(Integer contract) {
        this.contract = contract;
    }

    public Integer getOsNumber() {
        return osNumber;
    }

    public void setOsNumber(Integer osNumber) {
        this.osNumber = osNumber;
    }

    public String getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        this.occurrence = occurrence;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }

    public LocalDate getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(LocalDate screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Double getDistanceBaseOs() {
        return distanceBaseOs;
    }

    public void setDistanceBaseOs(Double distanceBaseOs) {
        this.distanceBaseOs = distanceBaseOs;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getResponsibleScreening() {
        return responsibleScreening;
    }

    public void setResponsibleScreening(String responsibleScreening) {
        this.responsibleScreening = responsibleScreening;
    }

    public boolean getIsEnable() {
        return isEnable;
    }


    public void setEnable(boolean enable) {
        isEnable = enable;
    }


    public TechnicalEntity getTechnical() {
        return technical;
    }

    public void setTechnical(TechnicalEntity technical) {
        this.technical = technical;
    }

    @Override
    public String area() {
        return area;
    }

    @Override
    public Double getLatitude() {
        return latitude;
    }

    @Override
    public Double getLongitude() {
        return longitude;
    }
}
