package pl.bkobeszko.b2bcalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bkobeszko.b2bcalculator.calculator.B2BCalculator;
import pl.bkobeszko.b2bcalculator.calculator.LinearTaxB2BCalculator;
import pl.bkobeszko.b2bcalculator.calculator.ScaleTaxB2BCalculator;
import pl.bkobeszko.b2bcalculator.calculator.summary.YearlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStoreFactory;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Component
public class B2BCalculatorComponent {
    
    @Autowired
    TaxFactorsStoreFactory taxFactorsStoreFactory;
    
    public YearlyCalculationSummary calculate(CalculatorInputData inputData) {
        TaxFactors taxFactors = taxFactorsStoreFactory.getTaxFactorsStore().getTaxFactorsForYear(inputData.getYear());
        B2BCalculator calculator;
        
        if (inputData.getTaxType() == TaxType.LINEAR) {
            calculator = new LinearTaxB2BCalculator();
        } else if (inputData.getTaxType() == TaxType.SCALE) {
            calculator = new ScaleTaxB2BCalculator();
        } else {
            // default
            calculator = new LinearTaxB2BCalculator();
        }
        
        return calculator.calculateYearlySummary(inputData, taxFactors);
    }
}
