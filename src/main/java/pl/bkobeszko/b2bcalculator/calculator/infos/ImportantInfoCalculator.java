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
public interface ImportantInfoCalculator {
    /**
     *
     * @param inputData
     * @param taxFactors
     * @param lastMonthSummary
     * @return optional empty if validation calculation is not necessary
     */
    Optional<ImportantInfo> calculate(CalculatorInputData inputData, TaxFactors taxFactors, MonthlyCalculationSummary lastMonthSummary);
}
