package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.CalculationStatistics;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.PeriodProfit;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class StatisticsTest extends B2BCalculatorComponentBaseTest {
    
    @Test
    public void testStatisticsCalculationForScaleTaxAndNormalZUS() {
        CalculationStatistics expected = CalculationStatistics.builder()
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(8385.61))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(8917.44), 1, 7),
                        new PeriodProfit(CalculatorUtils.getMoneyOf(8655.44), 8, 8),
                        new PeriodProfit(CalculatorUtils.getMoneyOf(7387.44), 9, 12)))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.SCALE)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
        
        CalculationStatistics actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testStatisticsCalculationForScaleTaxAndPreferentialZUS() {
        CalculationStatistics expected = CalculationStatistics.builder()
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(8850.77))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(9478.1), 1, 7),
                        new PeriodProfit(CalculatorUtils.getMoneyOf(8450.1), 8, 8),
                        new PeriodProfit(CalculatorUtils.getMoneyOf(7853.1), 9, 12)))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.SCALE)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .year(2017)
                .build();
        
        CalculationStatistics actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testStatisticsCalculationForLinearTaxAndNormalZUS() {
        CalculationStatistics expected = CalculationStatistics.builder()
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(8807.44))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(8807.44), 1, 12)))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .zusTaxType(ZUSTaxType.NORMAL)
                .year(2017)
                .build();
        
        CalculationStatistics actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testStatisticsThreeUniqueProfitsInScaleTaxWithThresholdReachedBeforeYearEnd() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        int actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics().getUniqueMonthlyProfits().size();
        assertThat(actual).isEqualTo(3);
    }
    
    @Test
    public void testStatisticsTwoUniqueProfitsInScaleTaxWithThresholdReachedExactlyAtYearEnd() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(8500)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        int actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics().getUniqueMonthlyProfits().size();
        assertThat(actual).isEqualTo(2);
    }
    
    @Test
    public void testStatisticsOneUniqueProfitsInScaleTaxWithThresholdNotReached() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(7000)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        int actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics().getUniqueMonthlyProfits().size();
        assertThat(actual).isEqualTo(1);
    }
    
    /**
     * LINEAR tax does not have threshold, so the profit is one all the year.
     */
    @Test
    public void testStatisticsOneUniqueProfitsInLinearTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(22000)
                .taxType(TaxType.LINEAR)
                .year(2017)
                .build();
        
        int actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics().getUniqueMonthlyProfits().size();
        assertThat(actual).isEqualTo(1);
    }
    
    @Test
    public void testStatisticsCalculationForLinearTaxAndPreferentialZUS() {
        CalculationStatistics expected = CalculationStatistics.builder()
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(9362.1))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(9362.10), 1, 12)))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .year(2017)
                .build();
        
        CalculationStatistics actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testStatisticsWithoutHealthInsurance() {
        CalculationStatistics expected = CalculationStatistics.builder()
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(8848.72))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(8848.72), 1, 12)))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .payZUSHealthInsurance(false)
                .year(2017)
                .build();
        
        CalculationStatistics actual = b2bCalculatorComponent.calculate(inputData).getCalculationStatistics();
        assertThat(actual).isEqualTo(expected);
    }
}
