package pl.bkobeszko.b2bcalculator.calculator;

import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class LinearTaxB2BCalculator extends B2BCalculator {
    
    @Override
    protected CalculationSummary calculateTax(CalculationSummary summary, TaxType taxType, TaxFactors taxFactors, CalculationSummary summaryCumulative) {
        // this tax rule is simple - one, constant tax rate throughout the year
        summary.setTax(CalculatorUtils.multiply(summary.getIncomeToTax(), taxFactors.getLinearIncomeTaxRate()));
        return summary;
    }
}
