package pl.bkobeszko.b2bcalculator.calculator.infos;

import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

import java.util.Optional;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ProportionalZUSInfoCalculator implements ImportantInfoCalculator {
    
    @Override
    public Optional<ImportantInfo> calculate(CalculatorInputData inputData, TaxFactors taxFactors, MonthlyCalculationSummary lastMonthSummary) {
        // TODO
        return Optional.empty();
    }
}
