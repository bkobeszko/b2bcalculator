package pl.bkobeszko.b2bcalculator.calculator.summary;

import lombok.Builder;
import lombok.Data;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxFactory;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class CalculationSummary {
    @Builder.Default
    private ZUSTax zus = ZUSTaxFactory.getEmptyZUS();
    
    private boolean scaleTaxThresholdReached;
    
    @Builder.Default
    private Money vat = CalculatorUtils.getZeroMoney();
    
    /**
     * Całkowita kwota faktury razem z podatkiem VAT.
     */
    @Builder.Default
    private Money totalInvoiceSum = CalculatorUtils.getZeroMoney();
    
    /**
     * Dochód do opodatkowania.
     */
    @Builder.Default
    private Money incomeToTax = CalculatorUtils.getZeroMoney();
    
    /**
     * Podatek.
     */
    @Builder.Default
    private Money tax = CalculatorUtils.getZeroMoney();
    
    /**
     * Zaliczka PIT.
     */
    @Builder.Default
    private Money advancePaymentPIT = CalculatorUtils.getZeroMoney();
    
    /**
     * Kwota "na rękę", po odtrąceniu podatków.
     */
    @Builder.Default
    private Money profit = CalculatorUtils.getZeroMoney();
    
    /**
     * Kwota netto na fakturze.
     */
    @Builder.Default
    private Money netInvoiceSum = CalculatorUtils.getZeroMoney();
    
    /**
     * Koszty uzyskania przychodu.
     */
    @Builder.Default
    private Money revenueCost = CalculatorUtils.getZeroMoney();
    
    public CalculationSummary add(CalculationSummary summary) {
        CalculationSummary added = CalculationSummary.builder().build();
        
        added.setZus(summary.getZus().add(getZus()));
        added.setScaleTaxThresholdReached(isScaleTaxThresholdReached());
        added.setVat(summary.getVat().plus(getVat()));
        added.setTotalInvoiceSum(summary.getTotalInvoiceSum().plus(getTotalInvoiceSum()));
        added.setIncomeToTax(summary.getIncomeToTax().plus(getIncomeToTax()));
        added.setTax(summary.getTax().plus(getTax()));
        added.setAdvancePaymentPIT(summary.getAdvancePaymentPIT().plus(getAdvancePaymentPIT()));
        added.setProfit(summary.getProfit().plus(getProfit()));
        added.setNetInvoiceSum(summary.getNetInvoiceSum().plus(getNetInvoiceSum()));
        added.setRevenueCost(summary.getRevenueCost().plus(getRevenueCost()));
        
        return added;
    }
}
