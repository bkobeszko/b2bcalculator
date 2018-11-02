package pl.bkobeszko.b2bcalculator.responses;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class MonthlyCalculationSummarySimplified {
    private int month;
    private CalculationSummarySimplified summary;
    private CalculationSummarySimplified summaryCumulative;
}
