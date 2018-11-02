package pl.bkobeszko.b2bcalculator.responses.statistics;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class CalculationStatisticsSimplified {
    private String averageMonthlyProfit;
    private List<PeriodProfitSimplified> uniqueMonthlyProfits;
}
