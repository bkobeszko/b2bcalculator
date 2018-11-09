package pl.bkobeszko.b2bcalculator.calculator.summary;

import lombok.Data;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
public class MonthlyCalculationSummary {
    private int month;
    private CalculationSummary summary;
    private CalculationSummary summaryCumulative;
    
    public MonthlyCalculationSummary(int month) {
        summary = CalculationSummary.builder().build();
        summaryCumulative = CalculationSummary.builder().build();
        this.month = month;
    }
}
