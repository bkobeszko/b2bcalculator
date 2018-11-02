package pl.bkobeszko.b2bcalculator.calculator.summary.statistics;

import lombok.Data;
import org.joda.money.Money;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
public class PeriodProfit {
    private final Money profit;
    private final Integer startMonth;
    private final Integer endMonth;
}
