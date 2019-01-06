package pl.bkobeszko.b2bcalculator.calculator.infos;

import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

import java.util.Optional;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class VATRequiredInfoCalculator implements ImportantInfoCalculator {
    
    /**
     * User must be a VAT payer if yearly turnover ("roczny obrót") is greater than the limit.
     * @param inputData
     * @param taxFactors
     * @param lastMonthSummary
     * @return
     */
    @Override
    public Optional<ImportantInfo> calculate(CalculatorInputData inputData, TaxFactors taxFactors, MonthlyCalculationSummary lastMonthSummary) {
        ImportantInfo importantInfo = null;
    
        if (!inputData.isPayVAT()) {
            importantInfo = calculateTurnoverInImportantInfo(lastMonthSummary.getSummaryCumulative(), taxFactors.getVatLimit());
        }
    
        return Optional.ofNullable(importantInfo);
    }
    
    private ImportantInfo calculateTurnoverInImportantInfo(CalculationSummary yearlySummary, Money vatLimit) {
        Money netInvoiceSum = yearlySummary.getNetInvoiceSum();
        Money advancePaymentPIT = yearlySummary.getAdvancePaymentPIT();
    
        Money turnover = netInvoiceSum.minus(advancePaymentPIT);
    
        ImportantInfo importantInfo = null;
        
        if (turnover.isGreaterThan(vatLimit)) {
            importantInfo = ImportantInfo.builder()
                    .type(ImportantInfo.Type.VATIsRequired)
                    .value(turnover)
                    .build();
        }
        
        return importantInfo;
    }
}
