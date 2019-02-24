package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ThresholdTest extends B2BCalculatorComponentBaseTest {
    
    @Test
    public void testThresholdReachedForScaleTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        List<MonthlyCalculationSummary> thresholdMonths = getMonthsWithThresholdReached(inputData);
        
        assertThat(thresholdMonths.size()).isEqualTo(5);
        assertThat(thresholdMonths.get(0).getMonth()).isEqualTo(8);
    }
    
    /**
     * The income is too low to reach the threshold.
     */
    @Test
    public void testThresholdNotReachedForScaleTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(8000)
                .taxType(TaxType.SCALE)
                .year(2017)
                .build();
        
        List<MonthlyCalculationSummary> thresholdMonths = getMonthsWithThresholdReached(inputData);
        
        assertThat(thresholdMonths.size()).isEqualTo(0);
    }
    
    private List<MonthlyCalculationSummary> getMonthsWithThresholdReached(CalculatorInputData inputData) {
        return b2bCalculatorComponent
                .calculate(inputData)
                .getMonthlySummaries()
                .stream()
                .filter(s -> s.getSummary().isScaleTaxThresholdReached())
                .collect(Collectors.toList());
    }
    
    /**
     * For linear tax type, the threshold does not exist, regardless of income.
     */
    @Test
    public void testThresholdNotReachForLinearTax() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(10000)
                .monthlyCosts(0)
                .taxType(TaxType.LINEAR)
                .payZUSDiseaseInsurance(false)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .year(2017)
                .build();
        
        List<MonthlyCalculationSummary> thresholdMonths = getMonthsWithThresholdReached(inputData);
        
        assertThat(thresholdMonths.size()).isEqualTo(0);
    }
}
