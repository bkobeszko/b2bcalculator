package pl.bkobeszko.b2bcalculator.calculator;

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
        Money incomeToTax = summaryCumulative.getIncomeToTax();
        Money afterThreshold = incomeToTax.plus(summary.getIncomeToTax()).minus(taxFactors.getScaleIncomeTaxThreshold());
        
        // checks if something is above the tax threshold
        if (afterThreshold.isGreaterThan(summary.getIncomeToTax())) {
            afterThreshold = summary.getIncomeToTax();
        }
        
        // if some (or every) part of our cumulative yearly income is above the tax threshold
        if (afterThreshold.isPositive()) {
            Money belowThreshold = summary.getIncomeToTax().minus(afterThreshold);
            
            // everything below threshold is taxed with first, lower tax rate
            if (belowThreshold.isGreaterThan(CalculatorUtils.getZeroMoney())) {
                // this is the month where the threshold is reached
                summary.setTax(summary.getTax().plus(CalculatorUtils.multiply(belowThreshold, taxFactors.getScaleIncomeTaxRate1())));
            }
            
            // everything above threshold (surplus) is taxed with second, greater tax rate
            summary.setTax(summary.getTax().plus(CalculatorUtils.multiply(afterThreshold, taxFactors.getScaleIncomeTaxRate2())));
            summary.setScaleTaxThresholdReached(true);
        } else {
            // our yearly cumulative income has not reached the tax threshold, so it's taxed with first, lower tax rate
            summary.setTax(CalculatorUtils.multiply(summary.getIncomeToTax(), taxFactors.getScaleIncomeTaxRate1()));
        }
        
        return summary;
    }
}
