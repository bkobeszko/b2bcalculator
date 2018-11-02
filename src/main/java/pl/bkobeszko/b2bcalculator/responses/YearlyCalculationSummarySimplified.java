package pl.bkobeszko.b2bcalculator.responses;

import lombok.Builder;
import lombok.Data;
import pl.bkobeszko.b2bcalculator.responses.statistics.CalculationStatisticsSimplified;

import java.util.List;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class YearlyCalculationSummarySimplified {
    private List<MonthlyCalculationSummarySimplified> monthlySummaries;
    private CalculationStatisticsSimplified calculationStatistics;
}
