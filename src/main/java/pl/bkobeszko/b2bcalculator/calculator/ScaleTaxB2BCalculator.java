package pl.bkobeszko.b2bcalculator.calculator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ScaleTaxB2BCalculator extends B2BCalculator {
    
    @Override
    protected CalculationSummary calculateTax(CalculationSummary summary, TaxType taxType, TaxFactors taxFactors, CalculationSummary summaryCumulative) {
        Money incomeToTax = summary.getIncomeToTax();
        Money incomeToTaxCumulative = summaryCumulative.getIncomeToTax();
        TaxData taxData;

        
        
        if (tooLowIncomeToTax(incomeToTax, taxFactors.getTaxFreeAmount())) {
            taxData = new TaxData(CalculatorUtils.getZeroMoney(), false);
        } else {
            taxData = calculateScaleTax(incomeToTax, summaryCumulative.getIncomeToTax(), summary.getTax(), taxFactors);
        }

        summary.setTax(taxData.tax);
        summary.setScaleTaxThresholdReached(taxData.scaleTaxThresholdReached);

        return summary;
    }
    
    private boolean tooLowIncomeToTax(Money incomeToTax, Money taxFreeAmount) {
        Money yearlyIncomeToTax = incomeToTax.multipliedBy(MONTHS_IN_YEAR);
        return yearlyIncomeToTax.isLessThan(taxFreeAmount) || yearlyIncomeToTax.isEqual(taxFreeAmount);
    }
    
    private TaxData calculateScaleTax(Money incomeToTax, Money incomeToTaxCumulative, Money tax, TaxFactors taxFactors) {
        TaxData taxData = new TaxData();
    
        Money afterThreshold = incomeToTaxCumulative.plus(incomeToTax).minus(taxFactors.getScaleIncomeTaxThreshold());
    
        // checks if something is above the tax threshold
        if (afterThreshold.isGreaterThan(incomeToTax)) {
            afterThreshold = incomeToTax;
        }
    
        // if some (or every) part of our cumulative yearly income is above the tax threshold
        if (afterThreshold.isPositive()) {
            Money belowThreshold = incomeToTax.minus(afterThreshold);
        
            // everything below threshold is taxed with first, lower tax rate
            if (belowThreshold.isGreaterThan(CalculatorUtils.getZeroMoney())) {
                // this is the month where the threshold is reached
                taxData.tax = tax.plus(CalculatorUtils.multiply(belowThreshold, taxFactors.getScaleIncomeTaxRate1()));
            }
        
            // everything above threshold (surplus) is taxed with second, greater tax rate
            taxData.tax = taxData.tax.plus(CalculatorUtils.multiply(afterThreshold, taxFactors.getScaleIncomeTaxRate2()));
            taxData.scaleTaxThresholdReached = true;
        } else {
            // our yearly cumulative income has not reached the tax threshold, so it's taxed with first, lower tax rate
            taxData.tax = CalculatorUtils.multiply(incomeToTax, taxFactors.getScaleIncomeTaxRate1());
        }
    
        return taxData;
    }
    
    @AllArgsConstructor
    @NoArgsConstructor
    private class TaxData {
        Money tax = CalculatorUtils.getZeroMoney();
        boolean scaleTaxThresholdReached = false;
    }
}
