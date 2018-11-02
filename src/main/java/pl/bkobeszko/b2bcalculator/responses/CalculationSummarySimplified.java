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
public class CalculationSummarySimplified {
    private ZUSTaxSimplified zus;
    private boolean scaleTaxThresholdReached;
    private String vat;
    private String totalInvoiceSum;
    private String incomeToTax;
    private String tax;
    private String advancePaymentPIT;
    private String profit;
    private String netInvoiceSum;
    private String revenueCost;
}
