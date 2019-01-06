package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class SummaryTest extends B2BCalculatorComponentBaseTest {
    
    @Test
    public void testSummaryScaleTaxOfMonthBeforeThresholdReached() {
        ZUSTax zusTax = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(812.61))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1172.56))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(875.28))
                .build();
        
        CalculationSummary expected = CalculationSummary.builder()
                .profit(CalculatorUtils.getMoneyOf(8917.44))
                .tax(CalculatorUtils.getMoneyOf(1710.46))
                .advancePaymentPIT(CalculatorUtils.getMoneyOf(1710))
                .incomeToTax(CalculatorUtils.getMoneyOf(10924.72))
                .netInvoiceSum(CalculatorUtils.getMoneyOf(12000))
                .revenueCost(CalculatorUtils.getMoneyOf(200))
                .vat(CalculatorUtils.getMoneyOf(2760))
                .totalInvoiceSum(CalculatorUtils.getMoneyOf(14760))
                .scaleTaxThresholdReached(false)
                .zus(zusTax)
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        CalculationSummary actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(6).getSummary();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testSummaryScaleTaxOfMonthWithThresholdReached() {
        ZUSTax zusTax = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(812.61))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1172.56))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(875.28))
                .build();
        
        CalculationSummary expected = CalculationSummary.builder()
                .profit(CalculatorUtils.getMoneyOf(8655.44))
                .tax(CalculatorUtils.getMoneyOf(1972.22))
                .advancePaymentPIT(CalculatorUtils.getMoneyOf(1972.00))
                .incomeToTax(CalculatorUtils.getMoneyOf(10924.72))
                .netInvoiceSum(CalculatorUtils.getMoneyOf(12000))
                .revenueCost(CalculatorUtils.getMoneyOf(200))
                .vat(CalculatorUtils.getMoneyOf(2760))
                .totalInvoiceSum(CalculatorUtils.getMoneyOf(14760))
                .scaleTaxThresholdReached(true)
                .zus(zusTax)
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        CalculationSummary actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(7).getSummary();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testSummaryScaleTaxOfMonthAfterThresholdReached() {
        ZUSTax zusTax = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(812.61))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1172.56))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(875.28))
                .build();
        
        CalculationSummary expected = CalculationSummary.builder()
                .profit(CalculatorUtils.getMoneyOf(7387.44))
                .tax(CalculatorUtils.getMoneyOf(3239.92))
                .advancePaymentPIT(CalculatorUtils.getMoneyOf(3240.00))
                .incomeToTax(CalculatorUtils.getMoneyOf(10924.72))
                .netInvoiceSum(CalculatorUtils.getMoneyOf(12000))
                .revenueCost(CalculatorUtils.getMoneyOf(200))
                .vat(CalculatorUtils.getMoneyOf(2760))
                .totalInvoiceSum(CalculatorUtils.getMoneyOf(14760))
                .scaleTaxThresholdReached(true)
                .zus(zusTax)
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        CalculationSummary actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(8).getSummary();
        assertThat(actual).isEqualTo(expected);
    }
    
    /**
     * Summaries for linear tax must be the same all year round.
     */
    @Test
    public void testSummaryLinearTax() {
        ZUSTax zusTax = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(812.61))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1172.56))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(875.28))
                .build();
        
        CalculationSummary expected = CalculationSummary.builder()
                .profit(CalculatorUtils.getMoneyOf(8807.44))
                .tax(CalculatorUtils.getMoneyOf(1819.71))
                .advancePaymentPIT(CalculatorUtils.getMoneyOf(1820.00))
                .incomeToTax(CalculatorUtils.getMoneyOf(10924.72))
                .netInvoiceSum(CalculatorUtils.getMoneyOf(12000))
                .revenueCost(CalculatorUtils.getMoneyOf(200))
                .vat(CalculatorUtils.getMoneyOf(2760))
                .totalInvoiceSum(CalculatorUtils.getMoneyOf(14760))
                .scaleTaxThresholdReached(false)
                .zus(zusTax)
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .year(2017)
                .build();
        
        List<MonthlyCalculationSummary> monthlySummaries = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries();
        
        CalculationSummary january = monthlySummaries.get(0).getSummary();
        CalculationSummary july = monthlySummaries.get(6).getSummary();
        CalculationSummary august = monthlySummaries.get(7).getSummary();
        CalculationSummary november = monthlySummaries.get(10).getSummary();
        
        assertThat(january).isEqualTo(expected);
        assertThat(july).isEqualTo(expected);
        assertThat(august).isEqualTo(expected);
        assertThat(november).isEqualTo(expected);
    }
}
