package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.joda.money.Money;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.YearlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class TaxFreeTest extends B2BCalculatorComponentBaseTest {
    
    @Test
    public void testTooLowYearlyIncomeZeroTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(200)
                .taxType(TaxType.SCALE)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
    
        Money expected = CalculatorUtils.getZeroMoney();
        Money actual = b2bCalculatorComponent.calculate(inputData).getYearlySummary().getTax();
        
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    @Ignore
    public void testSlightlyAboveTaxFreeThreshold() {
        CalculatorInputData inputData = CalculatorInputData.builder()
//                .monthlyNetIncome(1005) // 2009
                .monthlyNetIncome(1005)
                .taxType(TaxType.SCALE)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
    
        Money expected = CalculatorUtils.getMoneyOf(10000);
        Money actual = b2bCalculatorComponent.calculate(inputData).getYearlySummary().getTax();

        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    @Ignore
    public void testTooLowYearlyIncomeButLinearTaxType() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(200)
                .taxType(TaxType.LINEAR)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
    
        Money expected = CalculatorUtils.getMoneyOf(10000);
        Money actual = b2bCalculatorComponent.calculate(inputData).getYearlySummary().getTax();
    
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testZeroYearlyIncomeZeroTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(0)
                .taxType(TaxType.SCALE)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
    
        Money expected = CalculatorUtils.getZeroMoney();
        Money actual = b2bCalculatorComponent.calculate(inputData).getYearlySummary().getTax();
    
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testHighYearlyNetIncomeHighCostsZeroTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(20000)
                .monthlyCosts(19800)
                .taxType(TaxType.SCALE)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
    
        Money expected = CalculatorUtils.getZeroMoney();
        Money actual = b2bCalculatorComponent.calculate(inputData).getYearlySummary().getTax();
    
        assertThat(actual).isEqualTo(expected);
    }
}
