package pl.bkobeszko.b2bcalculator.calculator.infos;

import com.google.common.collect.Sets;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ImportantInfoChecker {
    private Set<ImportantInfoCalculator> importantInfoCalculators = Sets.newHashSet(
            new ProportionalZUSInfoCalculator(),
            new VATRequiredInfoCalculator()
    );
    
    public List<ImportantInfo> prepareImportantInfo(CalculatorInputData inputData, TaxFactors taxFactors, List<MonthlyCalculationSummary> summariesMonth) {
        MonthlyCalculationSummary lastMonthSummary = summariesMonth.get(summariesMonth.size() - 1);
    
        List<ImportantInfo> importantInfos = new ArrayList<>();
        
        for (ImportantInfoCalculator infoCalculator : importantInfoCalculators) {
            Optional<ImportantInfo> importantInfo = infoCalculator.calculate(inputData, taxFactors, lastMonthSummary);
    
            if (importantInfo.isPresent()) {
                importantInfos.add(importantInfo.get());
            }
        }
        
        return importantInfos;
    }
}
