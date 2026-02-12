package com.example.demo.model.entitys;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

@Entity
public class AccumulatedExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Double moneyAccumolated;
    Integer osNumber;

    public AccumulatedExpenseEntity() {
    }

    public AccumulatedExpenseEntity(Integer id, Double moneyAccumolated,Integer osNumber) {
        this.id = id;
        this.moneyAccumolated = moneyAccumolated;
        this.osNumber=osNumber;
    }

    public AccumulatedExpenseEntity(Double moneyAccumolated) {
        this.moneyAccumolated = moneyAccumolated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMoneyAccumolated() {
        return moneyAccumolated;
    }

    public void setMoneyAccumolated(Double moneyAccumolated) {
        this.moneyAccumolated = moneyAccumolated;
    }

    public Integer getOsNumber() {
        return osNumber;
    }

    public void setOsNumber(Integer osNumber) {
        this.osNumber = osNumber;
    }
}
