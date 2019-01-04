package pl.bkobeszko.b2bcalculator.responses.statistics;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class PeriodProfitSimplified {
    private String profit;
    private int startMonth;
    private int endMonth;
}
