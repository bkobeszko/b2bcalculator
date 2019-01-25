package pl.bkobeszko.b2bcalculator.taxinformation;

import lombok.Data;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.ZUSTaxRule;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.taxinformation.zus.ZUSTaxFactors;
import pl.bkobeszko.b2bcalculator.taxinformation.zus.ZUSTaxFactorsStore;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
public class TaxFactorsStore extends YearMapStore<TaxFactors> {
    
    private Map<Integer, Double> vatRate;
    private Map<Integer, Double> vatLimit;
    private Map<Integer, Double> linearIncomeTaxRate;
    private Map<Integer, Double> scaleIncomeTaxRate1;
    private Map<Integer, Double> scaleIncomeTaxRate2;
    private Map<Integer, Double> scaleIncomeTaxThreshold;
    private Map<Integer, Double> taxFreeAmount;
    private Map<Integer, Double> minimumSalary;
    private Map<Integer, Double> estimatedAvgMonthlySalary;
    private Map<Integer, Double> avgMonthlySalaryWithGainLastQt;
    private Map<Integer, String> zusTaxRule;
    private Map<Integer, Double> socialBasisFactorNormalZUS;
    private Map<Integer, Double> socialBasisFactorPreferentialZUS;
    private ZUSTaxFactorsStore normalZUSTaxFactorsStore;
    private ZUSTaxFactorsStore preferentialZUSTaxFactorsStore;
    
    @Override
    public TaxFactors getTaxFactorsForYear(int year) {
        Double currentVATRate = getDataDoubleByYear(vatRate, year);
        Money currentVATLimit = CalculatorUtils.getMoneyOf(getDataDoubleByYear(vatLimit, year));
        Double currentLinearIncomeTaxRate = getDataDoubleByYear(linearIncomeTaxRate, year);
        Double currentScaleIncomeTaxRate1 = getDataDoubleByYear(scaleIncomeTaxRate1, year);
        Double currentScaleIncomeTaxRate2 = getDataDoubleByYear(scaleIncomeTaxRate2, year);
        Money currentScaleIncomeTaxThreshold = CalculatorUtils.getMoneyOf(getDataDoubleByYear(scaleIncomeTaxThreshold, year));
        Money currentTaxFreeAmount = CalculatorUtils.getMoneyOf(getDataDoubleByYear(taxFreeAmount, year));
        Money currentMinimumSalary = CalculatorUtils.getMoneyOf(getDataDoubleByYear(minimumSalary, year));
        Money currentEstimatedAvgMonthlySalary = CalculatorUtils.getMoneyOf(getDataDoubleByYear(estimatedAvgMonthlySalary, year));
        Money currentAvgMonthlySalaryWithGainLastQt = CalculatorUtils.getMoneyOf(getDataDoubleByYear(avgMonthlySalaryWithGainLastQt, year));
        ZUSTaxRule currentZUSTaxRule = ZUSTaxRule.valueOf(getDataStringByYear(zusTaxRule, year));
        Double currentSocialBasisFactorNormalZUS = getDataDoubleByYear(socialBasisFactorNormalZUS, year);
        Double currentSocialBasisFactorPreferentialZUS = getDataDoubleByYear(socialBasisFactorPreferentialZUS, year);
        ZUSTaxFactors normalZUS = normalZUSTaxFactorsStore.getTaxFactorsForYear(year);
        ZUSTaxFactors preferentialZUS = preferentialZUSTaxFactorsStore.getTaxFactorsForYear(year);
    
        return TaxFactors.builder()
                .vatRate(currentVATRate)
                .vatLimit(currentVATLimit)
                .linearIncomeTaxRate(currentLinearIncomeTaxRate)
                .scaleIncomeTaxRate1(currentScaleIncomeTaxRate1)
                .scaleIncomeTaxRate2(currentScaleIncomeTaxRate2)
                .scaleIncomeTaxThreshold(currentScaleIncomeTaxThreshold)
                .taxFreeAmount(currentTaxFreeAmount)
                .minimumSalary(currentMinimumSalary)
                .estimatedAvgMonthlySalary(currentEstimatedAvgMonthlySalary)
                .avgMonthlySalaryWithGainLastQt(currentAvgMonthlySalaryWithGainLastQt)
                .zusTaxRule(currentZUSTaxRule)
                .socialBasisFactorNormalZUS(currentSocialBasisFactorNormalZUS)
                .socialBasisFactorPreferentialZUS(currentSocialBasisFactorPreferentialZUS)
                .normalZUS(normalZUS)
                .preferentialZUS(preferentialZUS)
                .build();
    }
    
    @Override
    public Set<Integer> getYears() {
        Set<Integer> years = new HashSet<>();
        
        years.addAll(vatRate.keySet());
        years.addAll(vatLimit.keySet());
        years.addAll(linearIncomeTaxRate.keySet());
        years.addAll(scaleIncomeTaxRate1.keySet());
        years.addAll(scaleIncomeTaxRate2.keySet());
        years.addAll(scaleIncomeTaxThreshold.keySet());
        years.addAll(taxFreeAmount.keySet());
        years.addAll(minimumSalary.keySet());
        years.addAll(estimatedAvgMonthlySalary.keySet());
        years.addAll(avgMonthlySalaryWithGainLastQt.keySet());
        years.addAll(zusTaxRule.keySet());
        years.addAll(socialBasisFactorNormalZUS.keySet());
        years.addAll(socialBasisFactorPreferentialZUS.keySet());
        years.addAll(normalZUSTaxFactorsStore.getYears());
        years.addAll(preferentialZUSTaxFactorsStore.getYears());
        
        return years;
    }
}
