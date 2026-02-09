package com.example.demo.model.entitys;

import jakarta.persistence.*;

@Entity
public class AccumulatedExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Double moneyAccumolated;

    public AccumulatedExpenseEntity() {
    }

    public AccumulatedExpenseEntity(Integer id, Double moneyAccumolated) {
        this.id = id;
        this.moneyAccumolated = moneyAccumolated;
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
}
