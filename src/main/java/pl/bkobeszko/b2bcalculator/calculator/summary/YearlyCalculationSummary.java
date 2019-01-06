package pl.bkobeszko.b2bcalculator.calculator.summary;

import lombok.Data;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.CalculationStatistics;

import java.util.List;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
public class YearlyCalculationSummary {
    private final List<MonthlyCalculationSummary> monthlySummaries;
    private final CalculationStatistics calculationStatistics;
    private final List<ImportantInfo> importantInfos;
}
