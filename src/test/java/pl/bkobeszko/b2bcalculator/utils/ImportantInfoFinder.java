package pl.bkobeszko.b2bcalculator.utils;

import lombok.RequiredArgsConstructor;
import pl.bkobeszko.b2bcalculator.B2BCalculatorComponent;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;

import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@RequiredArgsConstructor
public class ImportantInfoFinder {
    private final ImportantInfo.Type type;
    
    public Optional<ImportantInfo> find(List<ImportantInfo> importantInfoList) {
        return importantInfoList
                .stream()
                .filter(ii -> ii.getType().equals(type))
                .findFirst();
    }
    
    public Optional<ImportantInfo> calculateAndFind(B2BCalculatorComponent b2bCalculatorComponent, CalculatorInputData inputData) {
        List<ImportantInfo> result = b2bCalculatorComponent.calculate(inputData).getImportantInfos();
        return find(result);
    }
}
