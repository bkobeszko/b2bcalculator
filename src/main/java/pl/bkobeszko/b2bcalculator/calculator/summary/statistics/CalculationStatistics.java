package pl.bkobeszko.b2bcalculator.calculator.summary.statistics;

import lombok.Builder;
import lombok.Data;
import org.joda.money.Money;

import java.util.List;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class CalculationStatistics {
    private Money averageMonthlyProfit;
    private List<PeriodProfit> uniqueMonthlyProfits;
}
