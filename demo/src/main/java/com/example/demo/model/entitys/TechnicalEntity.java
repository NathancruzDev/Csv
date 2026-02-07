package com.example.demo.model.entitys;

import com.example.demo.model.dtos.TechnicalDto;
import com.example.demo.repository.CoordinateInterface;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TechnicalEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


    String name;

    Integer osNumber;
    Integer contract;

    Double latitude;
    Double longitude;

    String car;
    //Kilometro que o carro faz por Litro = KmCarXL
    Double kmCarXL;

    boolean isWorking;

    public TechnicalEntity(Integer id,String name, Integer osNumber, Integer contract, Double latitude,
                           Double longitude, String car, Double kmCarXL,boolean isWorking) {
        this.id=id;
        this.name = name;
        this.osNumber = osNumber;
        this.contract = contract;
        this.latitude = latitude;
        this.longitude = longitude;
        this.car = car;
        this.kmCarXL = kmCarXL;
        this.isWorking = isWorking;
    }

    public TechnicalEntity(Integer id, Double kmCarXL, String car, Double longitude, Double latitude, Integer contract, String name, Integer osNumber) {
        this.id = id;
        this.kmCarXL = kmCarXL;
        this.car = car;
        this.longitude = longitude;
        this.latitude = latitude;
        this.contract = contract;
        this.name = name;
        this.osNumber = osNumber;
    }

    public TechnicalEntity(TechnicalDto technicalDto) {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOsNumber() {
        return osNumber;
    }

    public void setOsNumber(Integer osNumber) {
        this.osNumber = osNumber;
    }

    public Integer getContract() {
        return contract;
    }

    public void setContract(Integer contract) {
        this.contract = contract;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Double getKmXLCar() {
        return kmCarXL;
    }

    public void setKmXLCar(Double kmCarXL) {
        this.kmCarXL = kmCarXL;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }


}
