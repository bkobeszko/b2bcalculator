package pl.bkobeszko.b2bcalculator;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.CalculationStatistics;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.PeriodProfit;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStore;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStoreFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class B2BCalculatorComponentTest {
    static TaxFactorsStore taxFactorsStore;
    
    @Mock
    private TaxFactorsStoreFactory taxFactorsStoreFactory;
    
    @InjectMocks
    private B2BCalculatorComponent b2bCalculatorComponent;
    
    @BeforeClass
    public static void beforeClassSetUp() {
        taxFactorsStore = TaxFactorsStoreFactory.createTaxFactorsStore();
    }
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(taxFactorsStoreFactory.getTaxFactorsStore()).thenReturn(taxFactorsStore);
    }
    
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
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(8841.07))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(9466.4), 1, 7),
                        new PeriodProfit(CalculatorUtils.getMoneyOf(8454.4), 8, 8),
                        new PeriodProfit(CalculatorUtils.getMoneyOf(7843.4), 9, 12)))
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
                .payZUSHealthInsurance(false)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .year(2017)
                .build();
        
        List<MonthlyCalculationSummary> thresholdMonths = getMonthsWithThresholdReached(inputData);
        
        assertThat(thresholdMonths.size()).isEqualTo(0);
    }
    
    @Test
    public void testStatisticsCalculationForLinearTaxAndPreferentialZUS() {
        CalculationStatistics expected = CalculationStatistics.builder()
                .averageMonthlyProfit(CalculatorUtils.getMoneyOf(9350.4))
                .uniqueMonthlyProfits(Arrays.asList(
                        new PeriodProfit(CalculatorUtils.getMoneyOf(9350.4), 1, 12)))
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
                .workFund(CalculatorUtils.getMoneyOf(14.70))
                .total(CalculatorUtils.getMoneyOf(502.60))
                .contributionToDeductFromIncome(CalculatorUtils.getMoneyOf(205.32))
                .build();
        
        CalculatorInputData inputData = CalculatorInputData.builder()
                .monthlyNetIncome(12000)
                .monthlyCosts(200)
                .taxType(TaxType.LINEAR)
                .zusTaxType(ZUSTaxType.PREFERENTIAL)
                .year(2017)
                .build();
        
        ZUSTax actual = b2bCalculatorComponent.calculate(inputData).getMonthlySummaries().get(0).getSummary().getZus();
        
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
}