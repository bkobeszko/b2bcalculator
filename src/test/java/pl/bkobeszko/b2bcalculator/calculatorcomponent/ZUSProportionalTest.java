package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;
import pl.bkobeszko.b2bcalculator.calculator.summary.YearlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;
import pl.bkobeszko.b2bcalculator.utils.ImportantInfoFinder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ZUSProportionalTest extends B2BCalculatorComponentBaseTest {
    
    private ImportantInfoFinder importantInfoFinder = new ImportantInfoFinder(ZUS_COULD_BE_PROPORTIONAL);
    
    @Test
    public void testZUSProportional() {
        ImportantInfo importantInfosExpected = ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(CalculatorUtils.getMoneyOf(642.59))
                .build();
        
        ZUSTax zusTaxExpectedNormal = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(812.61))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1172.56))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(875.28))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(2000)
                .year(2017)
                .build();
        
        YearlyCalculationSummary yearlyCalculationSummary = b2bCalculatorComponent.calculate(inputData);
        ZUSTax zusTaxActual = yearlyCalculationSummary.getMonthlySummaries().get(0).getSummary().getZus();
        Optional<ImportantInfo> importantInfo = importantInfoFinder.find(yearlyCalculationSummary.getImportantInfos());
    
        assertThat(zusTaxActual).isEqualTo(zusTaxExpectedNormal);
        assertThat(importantInfo.isPresent()).isEqualTo(true);
        assertThat(importantInfo.get()).isEqualTo(importantInfosExpected);
    }
    
    @Test
    public void testZUSProportionalExactlyAtLimit() {
        ImportantInfo expected = ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(CalculatorUtils.getMoneyOf(1160.57))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(5000) // this is maximum for minimum salary = 2000 (2000*30=5000*12)
                .year(2017)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(true);
        assertThat(importantInfo.get()).isEqualTo(expected);
    }
    
    @Test
    public void testZUSProportionalTooHighIncome() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(5000.01) // one "grosz" higher
                .year(2017)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(false);
    }
    
    @Test
    public void testZUSProportionalOnlyForIncomeWithoutCosts() {
        // the "real" income is 4000 (20000-16000), which is in ZUS limit, but only "pure" net income is applicable
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(20000)
                .monthlyCosts(16000)
                .year(2017)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(false);
    }
    
    @Test
    public void testZUSProportionalBelowLimitButOnlyConstantZUSInYear() {
        // proportional exists since 2017 (for test of course)
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(2000)
                .year(2016)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(false);
    }
    
    @Test
    public void testZUSProportionalCouldNotBeHigherThanNormal() {
        // calculated proportional ZUS is 1282.23, but is higher than normal ZUS in this test case
        
        ImportantInfo expected = ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(CalculatorUtils.getMoneyOf(644.83))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(2500) // in this year the minimum salary and social basis factor is lower for test purposes
                .year(2019)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(true);
        assertThat(importantInfo.get()).isEqualTo(expected);
    }
    
    @Test
    public void testZUSProportionalCouldNotBeLowerThanPreferential() {
        // calculated proportional is 452.68, but could not be lower than preferential
        
        ImportantInfo expected = ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(CalculatorUtils.getMoneyOf(487.90))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(900)
                .year(2017)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(true);
        assertThat(importantInfo.get()).isEqualTo(expected);
    }
    
    @Test
    public void testZUSProportionalAtLeapYear() {
        // at leap year the ZUS basis is little lower than usual
        
        ImportantInfo expected = ImportantInfo.builder()
                .type(ImportantInfo.Type.ZUS_COULD_BE_PROPORTIONAL)
                .value(CalculatorUtils.getMoneyOf(644.83))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(2000)
                .year(2020)
                .build();
    
        Optional<ImportantInfo> importantInfo = importantInfoFinder.calculateAndFind(b2bCalculatorComponent, inputData);
    
        assertThat(importantInfo.isPresent()).isEqualTo(true);
        assertThat(importantInfo.get()).isEqualTo(expected);
    }
}
