package com.example.demo.services;

import com.example.demo.model.entitys.AccumulatedExpenseEntity;
import com.example.demo.repository.AccumulatedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalcAmountTemp {
    @Autowired
    AccumulatedRepository accumulatedRepository;

    /*public Double getAllAmountTemp(){
        return accumulatedRepository.findAll().stream().map(AccumulatedExpenseEntity::getMoneyAccumolated).reduce(0.0, Double::sum);
    }*/

    public Double getAllAccumulated(){
        List<Double> listOfEntityes=accumulatedRepository.findAll().stream().map(
                x -> x.getMoneyAccumolated()).toList();
        Double addPlus=listOfEntityes.stream().reduce(0.00,(subtotal, elemento) -> subtotal + elemento);
        return addPlus;
    }
}
