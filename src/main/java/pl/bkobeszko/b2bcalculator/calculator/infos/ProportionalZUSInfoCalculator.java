package pl.bkobeszko.b2bcalculator.calculator.infos;

import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.ZUSTaxRule;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxFactory;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Year;
import java.util.Optional;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ProportionalZUSInfoCalculator implements ImportantInfoCalculator {
    
    private static final BigDecimal MONTHLY_INCOME_MULTIPLIER = BigDecimal.valueOf(30);
    private static final int PROPORTIONAL_ZUS_LIMIT_MULTIPLIER = 30;
    private static final BigDecimal SPECIAL_FACTOR_MULTIPLIER = BigDecimal.valueOf(0.24);
    private static final MathContext INTEGER_OPERATIONS_MATH_CONTEXT = MathContext.DECIMAL32;
    
    @Override
    public Optional<ImportantInfo> calculate(CalculatorInputData inputData, TaxFactors taxFactors, MonthlyCalculationSummary lastMonthSummary) {
        ImportantInfo importantInfo = null;
    
        if (taxFactors.getZusTaxRule() == ZUSTaxRule.CONSTANT_OR_PROPORTIONAL) {
            Money yearlyNetIncome = lastMonthSummary.getSummaryCumulative().getNetInvoiceSum();
            boolean inLimit = compareWithLimit(yearlyNetIncome, taxFactors.getMinimumSalary());
            
            if (inLimit) {
                importantInfo = calculateProportionalZUSInImportantInfo(inputData.getYear(), yearlyNetIncome, taxFactors, inputData.isPayZUSDiseaseInsurance());
            }
        }
    
        return Optional.ofNullable(importantInfo);
    }
    
    private boolean compareWithLimit(Money yearlyNetIncome, Money minimumSalary) {
        Money proportionalZUSLimit = CalculatorUtils.multiply(minimumSalary, PROPORTIONAL_ZUS_LIMIT_MULTIPLIER);
    
        return yearlyNetIncome.isLessThan(proportionalZUSLimit) || yearlyNetIncome.isEqual(proportionalZUSLimit);
    }
    
    private ImportantInfo calculateProportionalZUSInImportantInfo(int year, Money yearlyNetIncome, TaxFactors taxFactors, boolean includeHealthInsurance) {
        Money averageMonthlyIncome = calculateAverageMonthlyIncome(year, yearlyNetIncome);
        
        BigDecimal specialFactor = taxFactors
                .getEstimatedAvgMonthlySalary().getAmount()
                .divide(taxFactors.getMinimumSalary().getAmount(), INTEGER_OPERATIONS_MATH_CONTEXT)
                .multiply(SPECIAL_FACTOR_MULTIPLIER, INTEGER_OPERATIONS_MATH_CONTEXT);
    
        Money zusBasis = CalculatorUtils.multiply(averageMonthlyIncome, specialFactor);
    
        ZUSTax normalZUS = ZUSTaxFactory.getNormalZUS(taxFactors, includeHealthInsurance);
        ZUSTax preferentialZUS = ZUSTaxFactory.getPreferentialZUS(taxFactors, includeHealthInsurance);
        ZUSTax proportionalZUS = ZUSTaxFactory.getProportionalZUS(taxFactors, zusBasis, includeHealthInsurance);
    
        Money calculatedTotalZUS;
        if (proportionalZUS.getTotal().isLessThan(preferentialZUS.getTotal())) {
            calculatedTotalZUS = preferentialZUS.getTotal();
        } else if (proportionalZUS.getTotal().isGreaterThan(normalZUS.getTotal())) {
            calculatedTotalZUS = normalZUS.getTotal();
        } else {
            calculatedTotalZUS = proportionalZUS.getTotal();
        }
    
        return ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(calculatedTotalZUS)
                .build();
    }
    
    private Money calculateAverageMonthlyIncome(int year, Money yearlyNetIncome) {
        BigDecimal daysInYear = BigDecimal.valueOf(Year.of(year).length());
    
        BigDecimal average = yearlyNetIncome.getAmount()
                .divide(daysInYear, INTEGER_OPERATIONS_MATH_CONTEXT)
                .multiply(MONTHLY_INCOME_MULTIPLIER, INTEGER_OPERATIONS_MATH_CONTEXT);
        
        return CalculatorUtils.getMoneyOf(average);
    }
}
