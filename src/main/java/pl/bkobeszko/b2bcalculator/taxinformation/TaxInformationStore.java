package pl.bkobeszko.b2bcalculator.taxinformation;

import lombok.Data;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
public class TaxInformationStore {
    private static final Integer DEFAULT_YEAR = 2018;
    
    private Map<Integer, Double> vatRate;
    private Map<Integer, Double> linearIncomeTaxRate;
    private Map<Integer, Double> scaleIncomeTaxRate1;
    private Map<Integer, Double> scaleIncomeTaxRate2;
    private Map<Integer, Double> scaleIncomeTaxThreshold;
    private Map<Integer, Double> zusSocialInsurance;
    private Map<Integer, Double> zusSocialInsurancePreferential;
    private Map<Integer, Double> zusHealthInsuranceContributionBasis;
    private Map<Integer, Double> zusHealthInsuranceContributionBasisRate;
    private Map<Integer, Double> zusHealthInsuranceContributionToDeductRate;
    private Map<Integer, Double> zusWorkFund;
    private Map<Integer, Double> zusWorkFundPreferential;
    
    public TaxInformation getTaxInformationForYear(int year) {
        Double currentVATRate = getDataByYear(vatRate, year);
        Double currentLinearIncomeTaxRate = getDataByYear(linearIncomeTaxRate, year);
        Double currentScaleIncomeTaxRate1 = getDataByYear(scaleIncomeTaxRate1, year);
        Double currentScaleIncomeTaxRate2 = getDataByYear(scaleIncomeTaxRate2, year);
        Money currentScaleIncomeTaxThreshold = CalculatorUtils.getMoneyOf(getDataByYear(scaleIncomeTaxThreshold, year));
        Money currentZUSSocialInsurance = CalculatorUtils.getMoneyOf(getDataByYear(zusSocialInsurance, year));
        Money currentZUSSocialInsurancePreferential = CalculatorUtils.getMoneyOf(getDataByYear(zusSocialInsurancePreferential, year));
        Money currentZUSHealthInsuranceContributionBasis = CalculatorUtils.getMoneyOf(getDataByYear(zusHealthInsuranceContributionBasis, year));
        Double currentZUSHealthInsuranceContributionBasisRate = getDataByYear(zusHealthInsuranceContributionBasisRate, year);
        Double currentZUSHealthInsuranceContributionToDeductRate = getDataByYear(zusHealthInsuranceContributionToDeductRate, year);
        Money currentZUSWorkFund = CalculatorUtils.getMoneyOf(getDataByYear(zusWorkFund, year));
        Money currentZUSWorkFundPreferential = CalculatorUtils.getMoneyOf(getDataByYear(zusWorkFundPreferential, year));
        
        return TaxInformation.builder()
                .vatRate(currentVATRate)
                .linearIncomeTaxRate(currentLinearIncomeTaxRate)
                .scaleIncomeTaxRate1(currentScaleIncomeTaxRate1)
                .scaleIncomeTaxRate2(currentScaleIncomeTaxRate2)
                .scaleIncomeTaxThreshold(currentScaleIncomeTaxThreshold)
                .zusSocialInsurance(currentZUSSocialInsurance)
                .zusSocialInsurancePreferential(currentZUSSocialInsurancePreferential)
                .zusHealthInsuranceContributionBasis(currentZUSHealthInsuranceContributionBasis)
                .zusHealthInsuranceContributionBasisRate(currentZUSHealthInsuranceContributionBasisRate)
                .zusHealthInsuranceContributionToDeductRate(currentZUSHealthInsuranceContributionToDeductRate)
                .zusWorkFund(currentZUSWorkFund)
                .zusWorkFundPreferential(currentZUSWorkFundPreferential)
                .build();
    }
    
    public Set<Integer> getYears() {
        Set<Integer> years = new HashSet<>();
        
        years.addAll(vatRate.keySet());
        years.addAll(linearIncomeTaxRate.keySet());
        years.addAll(scaleIncomeTaxRate1.keySet());
        years.addAll(scaleIncomeTaxRate2.keySet());
        years.addAll(scaleIncomeTaxThreshold.keySet());
        years.addAll(zusSocialInsurancePreferential.keySet());
        years.addAll(zusHealthInsuranceContributionBasis.keySet());
        years.addAll(zusHealthInsuranceContributionBasisRate.keySet());
        years.addAll(zusHealthInsuranceContributionToDeductRate.keySet());
        years.addAll(zusWorkFund.keySet());
        years.addAll(zusWorkFundPreferential.keySet());
        
        return years;
    }
    
    public List<Integer> getYearsSortedDescending() {
        return getYears()
                .stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
    
    private Double getDataByYear(Map<Integer, Double> dataMap, int year) {
        // get minimal value from key set
        Integer minYear = dataMap
                .keySet()
                .stream()
                .min(Integer::compare)
                .orElse(DEFAULT_YEAR);
        
        // get year lower or equal than parameter or minimal year
        Integer keyYear = dataMap
                .keySet()
                .stream()
                .filter(key -> key <= year)
                .max(Integer::compare)
                .orElse(minYear);
        
        return dataMap.get(keyYear);
    }
}
