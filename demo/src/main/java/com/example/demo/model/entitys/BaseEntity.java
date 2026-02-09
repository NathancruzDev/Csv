package com.example.demo.model.entitys;

import com.example.demo.repository.CoordinateInterface;
import com.example.demo.model.UnitEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BaseEntity implements CoordinateInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private UnitEnum unit;
    private String adress;
    private String area;
    private String Latitude;
    private String Longitude;


    public BaseEntity(Integer id,UnitEnum unit, String adress, String area, String latitude, String longitude) {
        this.id=id;
        this.unit = unit;
        this.adress = adress;
        this.area = area;
        Latitude = latitude;
        Longitude = longitude;
    }

    public BaseEntity() {
    }

    public Integer getId() {
        return id;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }


    @Override
    public String area() {
        return "";
    }

    @Override
    public Double getLatitude() {
        return 0.0;
    }

    @Override
    public Double getLongitude() {
        return 0.0;
    }
}
