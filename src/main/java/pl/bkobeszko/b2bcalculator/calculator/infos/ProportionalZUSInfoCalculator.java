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
 * Copyright (c) 2021, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ProportionalZUSInfoCalculator implements ImportantInfoCalculator {

    private static final BigDecimal MONTHLY_INCOME_MULTIPLIER = BigDecimal.valueOf(30);
    private static final BigDecimal SPECIAL_FACTOR_MULTIPLIER = BigDecimal.valueOf(0.5);
    private static final MathContext INTEGER_OPERATIONS_MATH_CONTEXT = MathContext.DECIMAL32;
    
    @Override
    public Optional<ImportantInfo> calculate(CalculatorInputData inputData, TaxFactors taxFactors, MonthlyCalculationSummary lastMonthSummary) {
        ImportantInfo importantInfo = null;
    
        if (taxFactors.getZusTaxRule() == ZUSTaxRule.CONSTANT_OR_PROPORTIONAL) {
            Money yearlyRevenue = lastMonthSummary.getSummaryCumulative().getRevenue();
            boolean inLimit = compareWithLimit(yearlyRevenue, taxFactors.getProportionalLimitZUS());
            
            if (inLimit) {
                importantInfo = calculateProportionalZUSInImportantInfo(inputData.getYear(), yearlyRevenue, taxFactors, inputData.isPayZUSDiseaseInsurance());
            }
        }
    
        return Optional.ofNullable(importantInfo);
    }
    
    private boolean compareWithLimit(Money yearlyRevenue, double proportionalLimitZUSDouble) {
        Money proportionalLimitZUS = CalculatorUtils.getMoneyOf(proportionalLimitZUSDouble);

        return yearlyRevenue.isLessThan(proportionalLimitZUS) || yearlyRevenue.isEqual(proportionalLimitZUS);
    }
    
    private ImportantInfo calculateProportionalZUSInImportantInfo(int year, Money yearlyRevenue, TaxFactors taxFactors, boolean includeHealthInsurance) {
        Money averageMonthlyIncome = calculateAverageMonthlyIncome(year, yearlyRevenue);
        Money zusBasis = CalculatorUtils.multiply(averageMonthlyIncome, SPECIAL_FACTOR_MULTIPLIER);
    
        ZUSTax normalZUS = ZUSTaxFactory.getNormalZUS(taxFactors, includeHealthInsurance);
        ZUSTax preferentialZUS = ZUSTaxFactory.getPreferentialZUS(taxFactors, includeHealthInsurance);
        ZUSTax proportionalZUS = ZUSTaxFactory.getProportionalZUS(taxFactors, zusBasis, includeHealthInsurance);

        if (proportionalZUS.getTotal().isLessThan(preferentialZUS.getTotal())) {
            return buildInfoForProportional(preferentialZUS.getTotal());
        } else if (proportionalZUS.getTotal().isLessThan(normalZUS.getTotal())) {
            return buildInfoForProportional(proportionalZUS.getTotal());
        } else {
            return null;
        }
    }

    private ImportantInfo buildInfoForProportional(Money total) {
        return ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(total)
                .build();
    }

    private Money calculateAverageMonthlyIncome(int year, Money yearlyRevenue) {
        BigDecimal daysInYear = BigDecimal.valueOf(Year.of(year).length());
    
        BigDecimal average = yearlyRevenue.getAmount()
                .divide(daysInYear, INTEGER_OPERATIONS_MATH_CONTEXT)
                .multiply(MONTHLY_INCOME_MULTIPLIER, INTEGER_OPERATIONS_MATH_CONTEXT);
        
        return CalculatorUtils.getMoneyOf(average);
    }
}
