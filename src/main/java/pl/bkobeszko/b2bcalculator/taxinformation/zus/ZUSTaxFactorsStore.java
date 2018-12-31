package pl.bkobeszko.b2bcalculator.taxinformation.zus;

import lombok.Data;
import pl.bkobeszko.b2bcalculator.taxinformation.YearMapStore;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
public class ZUSTaxFactorsStore extends YearMapStore<ZUSTaxFactors> {
    
    private Map<Integer, Double> retirementBasisPart;
    private Map<Integer, Double> disabilityBasisPart;
    private Map<Integer, Double> diseaseBasisPart;
    private Map<Integer, Double> accidentBasisPart;
    private Map<Integer, Double> workFundBasisPart;
    private Map<Integer, Double> healthBasisFactor;
    private Map<Integer, Double> healthBasisPart;
    private Map<Integer, Double> healthBasisPartToDeductFromIncome;
    
    @Override
    public ZUSTaxFactors getTaxFactorsForYear(int year) {
        Double currentRetirementBasisPart = getDataDoubleByYear(retirementBasisPart, year);
        Double currentDisabilityBasisPart = getDataDoubleByYear(disabilityBasisPart, year);
        Double currentDiseaseBasisPart = getDataDoubleByYear(diseaseBasisPart, year);
        Double currentAccidentBasisPart = getDataDoubleByYear(accidentBasisPart, year);
        Double currentWorkFundBasisPart = getDataDoubleByYear(workFundBasisPart, year);
        Double currentHealthBasisFactor = getDataDoubleByYear(healthBasisFactor, year);
        Double currentHealthBasisPart = getDataDoubleByYear(healthBasisPart, year);
        Double currentHealthBasisPartToDeductFromIncome = getDataDoubleByYear(healthBasisPartToDeductFromIncome, year);
        
        return ZUSTaxFactors.builder()
                .retirementBasisPart(currentRetirementBasisPart)
                .disabilityBasisPart(currentDisabilityBasisPart)
                .diseaseBasisPart(currentDiseaseBasisPart)
                .accidentBasisPart(currentAccidentBasisPart)
                .workFundBasisPart(currentWorkFundBasisPart)
                .healthBasisFactor(currentHealthBasisFactor)
                .healthBasisPart(currentHealthBasisPart)
                .healthBasisPartToDeductFromIncome(currentHealthBasisPartToDeductFromIncome)
                .build();
    }
    
    @Override
    public Set<Integer> getYears() {
        Set<Integer> years = new HashSet<>();
    
        years.addAll(retirementBasisPart.keySet());
        years.addAll(disabilityBasisPart.keySet());
        years.addAll(diseaseBasisPart.keySet());
        years.addAll(accidentBasisPart.keySet());
        years.addAll(workFundBasisPart.keySet());
        years.addAll(healthBasisFactor.keySet());
        years.addAll(healthBasisPart.keySet());
        years.addAll(healthBasisPartToDeductFromIncome.keySet());
    
        return years;
    }
}
