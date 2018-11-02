package pl.bkobeszko.b2bcalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.bkobeszko.b2bcalculator.calculator.B2BCalculator;
import pl.bkobeszko.b2bcalculator.calculator.LinearTaxB2BCalculator;
import pl.bkobeszko.b2bcalculator.calculator.ScaleTaxB2BCalculator;
import pl.bkobeszko.b2bcalculator.calculator.summary.YearlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxInformation;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxInformationStoreFactory;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Component
public class B2BCalculatorComponent {

    @Autowired
    TaxInformationStoreFactory taxInformationStoreFactory;

    public YearlyCalculationSummary calculate(CalculatorInputData inputData) {
        TaxInformation taxInformation = taxInformationStoreFactory.getTaxInformationStore().getTaxInformationForYear(inputData.getYear());
        B2BCalculator calculator;

        if (inputData.getTaxType() == TaxType.LINEAR) {
            calculator = new LinearTaxB2BCalculator();
        } else if (inputData.getTaxType() == TaxType.SCALE) {
            calculator = new ScaleTaxB2BCalculator();
        } else {
            // default
            calculator = new LinearTaxB2BCalculator();
        }

        return calculator.calculateYearlySummary(inputData, taxInformation);
    }
}
