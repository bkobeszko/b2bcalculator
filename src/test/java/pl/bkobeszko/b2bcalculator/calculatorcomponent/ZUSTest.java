package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ZUSTest extends B2BCalculatorComponentBaseTest {
    
    @Test
    public void testZUSNormalTax() {
        ZUSTax expected = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(812.61))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1172.56))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(875.28))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .payZUSDiseaseInsurance(true)
                .year(2017)
                .build();
        
        ZUSTax actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary().getZus();
        
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testZUSPreferentialTax() {
        ZUSTax expected = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(190.62))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getZeroMoney())
                .total(CalculatorUtils.getMoneyOf(487.9))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(190.62))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .payZUSDiseaseInsurance(true)
                .year(2017)
                .build();
        
        ZUSTax actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary().getZus();
        
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testZUSNormalTaxWithoutDiseaseInsurance() {
        ZUSTax expected = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(749.94))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getMoneyOf(62.67))
                .total(CalculatorUtils.getMoneyOf(1109.89))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(812.61))
                .build();
    
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .zusTaxType(ZUSTaxType.NORMAL)
                .payZUSDiseaseInsurance(false)
                .year(2017)
                .build();
    
        ZUSTax actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary().getZus();
    
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    public void testZUSPreferentialTaxWithoutDiseaseInsurance() {
        ZUSTax expected = ZUSTax.builder()
                .socialInsurance(CalculatorUtils.getMoneyOf(175.92))
                .healthInsurance(CalculatorUtils.getMoneyOf(297.28))
                .healthInsuranceContributionToDeduct(CalculatorUtils.getMoneyOf(255.99))
                .workFund(CalculatorUtils.getZeroMoney())
                .total(CalculatorUtils.getMoneyOf(473.2))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(175.92))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .payZUSDiseaseInsurance(false)
                .year(2017)
                .build();
        
        ZUSTax actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary().getZus();
        
        assertThat(actual).isEqualTo(expected);
    }
}
