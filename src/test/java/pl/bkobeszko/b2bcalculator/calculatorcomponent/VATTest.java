package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class VATTest extends B2BCalculatorComponentBaseTest {
    
    @Test
    public void testWithPayingTheVAT() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .taxType(TaxType.LINEAR)
                .payVAT(true)
                .build();
        
        CalculationSummary actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary();
        
        assertThat(actual.getVat()).isEqualTo(CalculatorUtils.getMoneyOf(2760));
        assertThat(actual.getTotalInvoiceSum()).isEqualTo(CalculatorUtils.getMoneyOf(14760));
    }
    
    @Test
    public void testWithoutPayingTheVAT() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .taxType(TaxType.LINEAR)
                .payVAT(false)
                .year(2017)
                .build();
        
        CalculationSummary actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary();
        
        assertThat(actual.getVat()).isEqualTo(CalculatorUtils.getMoneyOf(0));
        assertThat(actual.getTotalInvoiceSum()).isEqualTo(CalculatorUtils.getMoneyOf(inputData.getMonthlyNetIncome()));
    }
    
    @Test
    public void testNotPayVATButAlmostRequired() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(20050)
                .taxType(TaxType.LINEAR)
                .payVAT(false)
                .year(2017)
                .build();
        
        List<ImportantInfo> actual = b2bCalculatorComponent.calculate(inputData).getImportantInfos();
        
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(0);
    }
    
    @Test
    public void testNotPayVATButRequired() {
        ImportantInfo expected = ImportantInfo.builder()
                .type(ImportantInfo.Type.VATIsRequired)
                .value(CalculatorUtils.getMoneyOf(200052))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(20060)
                .taxType(TaxType.LINEAR)
                .payVAT(false)
                .year(2017)
                .build();
        
        List<ImportantInfo> actual = b2bCalculatorComponent.calculate(inputData).getImportantInfos();
        
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(expected);
    }
    
    @Test
    public void testNotPayVATAndNotRequired() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(25000)
                .taxType(TaxType.LINEAR)
                .payVAT(true)
                .year(2017)
                .build();
        
        List<ImportantInfo> actual = b2bCalculatorComponent.calculate(inputData).getImportantInfos();
        
        assertThat(actual.size()).isEqualTo(0);
    }
    
    @Test
    public void testNotPayVATTooSmallTurnover() {
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(5000)
                .taxType(TaxType.LINEAR)
                .payVAT(false)
                .year(2017)
                .build();
        
        List<ImportantInfo> actual = b2bCalculatorComponent.calculate(inputData).getImportantInfos();
        
        assertThat(actual.size()).isEqualTo(0);
    }
}
