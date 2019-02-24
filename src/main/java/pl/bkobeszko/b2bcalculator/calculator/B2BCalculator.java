package pl.bkobeszko.b2bcalculator.calculator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.CalculatorInputData;
import pl.bkobeszko.b2bcalculator.TaxType;
import pl.bkobeszko.b2bcalculator.calculator.infos.ImportantInfoChecker;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.ImportantInfo;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.YearlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.CalculationStatistics;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.PeriodProfit;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTax;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxFactory;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Slf4j
public abstract class B2BCalculator {
    private static final int MONTHS_IN_YEAR = 12; // this is not supposed to change :)
    
    private ImportantInfoChecker importantInfoChecker = new ImportantInfoChecker();
    
    protected abstract CalculationSummary calculateTax(CalculationSummary summary, TaxType taxType, TaxFactors taxFactors, CalculationSummary summaryCumulative);
    
    public YearlyCalculationSummary calculateYearlySummary(CalculatorInputData inputData, TaxFactors taxFactors) {
        log.info("calculating yearly summary for {}", inputData);
        
        List<MonthlyCalculationSummary> summariesMonth = calculateMonthSummaries(inputData, taxFactors);
        CalculationStatistics statistics = calculateStatistics(summariesMonth);
        List<ImportantInfo> importantInfos = importantInfoChecker.prepareImportantInfo(inputData, taxFactors, summariesMonth);
        
        return new YearlyCalculationSummary(summariesMonth, statistics, importantInfos);
    }
    
    private CalculationStatistics calculateStatistics(List<MonthlyCalculationSummary> summariesMonth) {
        ImmutableListMultimap<CalculationSummary, MonthlyCalculationSummary> monthsWithSummary = Multimaps.index(summariesMonth, MonthlyCalculationSummary::getSummary);
        
        List<PeriodProfit> uniqueMonthlyProfits = new ArrayList<>();
        for (CalculationSummary calculationSummary : monthsWithSummary.keySet()) {
            ImmutableList<MonthlyCalculationSummary> summaries = monthsWithSummary.get(calculationSummary);
            
            List<Integer> months = summaries.stream()
                    .map(MonthlyCalculationSummary::getMonth)
                    .collect(Collectors.toList());
            
            Integer firstMonth = months.get(0);
            Integer lastMonth = months.get(months.size() - 1);
            
            uniqueMonthlyProfits.add(new PeriodProfit(calculationSummary.getProfit(), firstMonth, lastMonth));
        }
        
        // yearly profit is the last cumulated profit
        Money totalYearlyProfit = summariesMonth.get(summariesMonth.size() - 1).getSummaryCumulative().getProfit();
        Money averageMonthlyProfit = CalculatorUtils.divide(totalYearlyProfit, summariesMonth.size());
        
        return CalculationStatistics.builder()
                .averageMonthlyProfit(averageMonthlyProfit)
                .uniqueMonthlyProfits(uniqueMonthlyProfits)
                .build();
    }
    
    private List<MonthlyCalculationSummary> calculateMonthSummaries(CalculatorInputData inputData, TaxFactors taxFactors) {
        ZUSTax zus = inputData.getZusTaxType() == ZUSTaxType.NORMAL ? ZUSTaxFactory.getNormalZUS(taxFactors, inputData.isPayZUSDiseaseInsurance()) : ZUSTaxFactory.getPreferentialZUS(taxFactors, inputData.isPayZUSDiseaseInsurance());
        CalculationSummary summaryCumulativeTotal = CalculationSummary.builder().build();
        List<MonthlyCalculationSummary> summariesMonth = new ArrayList<>();
        
        for (int i = 0; i < MONTHS_IN_YEAR; i++) {
            CalculationSummary summary = calculateSummaryOneMonth(inputData, taxFactors, zus, summaryCumulativeTotal);
            
            MonthlyCalculationSummary month = new MonthlyCalculationSummary(i + 1);
            month.setSummary(summary);
            
            CalculationSummary cumulativeToCurrentMonth = month.getSummary().add(summaryCumulativeTotal);
            month.setSummaryCumulative(cumulativeToCurrentMonth);
            
            summaryCumulativeTotal = month.getSummaryCumulative();
            
            summariesMonth.add(month);
        }
        
        return summariesMonth;
    }
    
    private CalculationSummary calculateSummaryOneMonth(CalculatorInputData inputData, TaxFactors taxFactors, ZUSTax zus, CalculationSummary summaryCumulativeTotal) {
        // helper variables to get code more readable
        Money monthlyNetIncome = CalculatorUtils.getMoneyOf(inputData.getMonthlyNetIncome());
        Money monthlyCosts = CalculatorUtils.getMoneyOf(inputData.getMonthlyCosts());
        Money contributionToDeductFromIncome = zus.getContributionToDeductFromIncome();
        Money healthInsuranceContributionToDeduct = zus.getHealthInsuranceContributionToDeduct();
        Money zusTotal = zus.getTotal();
        
        CalculationSummary summaryOneMonth = CalculationSummary.builder()
                .zus(zus)
                .netInvoiceSum(monthlyNetIncome)
                .revenueCost(monthlyCosts).incomeToTax(monthlyNetIncome.minus(monthlyCosts).minus(contributionToDeductFromIncome))
                .build();
        
        /* Very important! Tax calculation must be directly after set the:
         * - zus
         * - netInvoiceSum
         * - revenueCost
         * - incomeToTax
         */
        summaryOneMonth = calculateTax(summaryOneMonth, inputData.getTaxType(), taxFactors, summaryCumulativeTotal);
        
        Money taxWithoutHealthContribution = summaryOneMonth.getTax().minus(healthInsuranceContributionToDeduct);
        
        // TODO simple hack, remove it when tax will be calculated with tax free amount etc.
        if (taxWithoutHealthContribution.isLessThan(CalculatorUtils.getZeroMoney())) {
            taxWithoutHealthContribution = CalculatorUtils.getZeroMoney();
        }
        
        summaryOneMonth.setTax(taxWithoutHealthContribution);
        summaryOneMonth.setAdvancePaymentPIT(calculateAdvancePaymentPIT(summaryOneMonth.getTax()));
        
        Money vat = CalculatorUtils.getZeroMoney();
        if (inputData.isPayVAT()) {
            vat = CalculatorUtils.multiply(monthlyNetIncome, taxFactors.getVatRate());
        }
        
        summaryOneMonth.setVat(vat);
        summaryOneMonth.setTotalInvoiceSum(monthlyNetIncome.plus(summaryOneMonth.getVat()));
        summaryOneMonth.setProfit(monthlyNetIncome.minus(monthlyCosts).minus(zusTotal).minus(summaryOneMonth.getAdvancePaymentPIT()));
        
        return summaryOneMonth;
    }
    
    /**
     * Every month we don't pay the whole tax, we pay only the advance,
     * which is the tax rounded to integer as follows:
     * - if the major after the point is <0:49>, then we round to the floor
     * - if the major after the point is 50 or greater, then we round to the ceil
     * <p>
     * For example: 541.37 returns 541; 541.49 returns 541; 541.50 returns 542; 541.0 returns 541
     *
     * @param tax
     * @return
     */
    private Money calculateAdvancePaymentPIT(Money tax) {
        Money advancePIT = tax;
        Money decimalPart = tax.minus(tax.getAmountMajor());
        
        if (decimalPart.isGreaterThan(CalculatorUtils.getHalfMoney())) {
            advancePIT = advancePIT.rounded(0, RoundingMode.CEILING);
        } else {
            advancePIT = advancePIT.rounded(0, RoundingMode.FLOOR);
        }
        
        return advancePIT;
    }
}
