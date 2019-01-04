package pl.bkobeszko.b2bcalculator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.joda.money.format.MoneyFormatter;
import org.joda.money.format.MoneyFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.bkobeszko.b2bcalculator.calculator.summary.CalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.MonthlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.YearlyCalculationSummary;
import pl.bkobeszko.b2bcalculator.calculator.summary.statistics.PeriodProfit;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;
import pl.bkobeszko.b2bcalculator.responses.CalculationSummarySimplified;
import pl.bkobeszko.b2bcalculator.responses.MonthlyCalculationSummarySimplified;
import pl.bkobeszko.b2bcalculator.responses.YearlyCalculationSummarySimplified;
import pl.bkobeszko.b2bcalculator.responses.ZUSTaxSimplified;
import pl.bkobeszko.b2bcalculator.responses.statistics.CalculationStatisticsSimplified;
import pl.bkobeszko.b2bcalculator.responses.statistics.PeriodProfitSimplified;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@RestController
@Slf4j
public class CalculatorController {
    
    @Autowired
    @Getter
    private B2BCalculatorComponent b2BCalculatorComponent;
    
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public YearlyCalculationSummarySimplified calculate(
            Locale locale,
            @RequestParam(value = "monthlyNetIncome") String monthlyNetIncome,
            @RequestParam(value = "monthlyCosts") String monthlyCosts,
            @RequestParam(value = "year") String year,
            @RequestParam(value = "payZUSHealthInsurance") String payZUSHealthInsurance,
            @RequestParam(value = "payVAT") String payVAT,
            @RequestParam(value = "taxType") String taxType,
            @RequestParam(value = "zusTaxType") String zusTaxType) {
        
        log.info("calculating invoked for {} monthly net income", monthlyNetIncome);
        
        CalculatorInputData calculatorInputData = CalculatorInputData.builder()
                .monthlyNetIncome(Double.parseDouble(monthlyNetIncome))
                .monthlyCosts(StringUtils.isEmpty(monthlyCosts) ? 0d : Double.parseDouble(monthlyCosts))
                .year(Integer.parseInt(year))
                .payZUSHealthInsurance(Boolean.parseBoolean(payZUSHealthInsurance))
                .payVAT(Boolean.parseBoolean(payVAT))
                .taxType(TaxType.parseEnum(taxType, TaxType.LINEAR))
                .zusTaxType(ZUSTaxType.parseEnum(zusTaxType, ZUSTaxType.NORMAL))
                .build();
        
        YearlyCalculationSummary calculationSummary = getB2BCalculatorComponent().calculate(calculatorInputData);
        return convertToSimplified(locale, calculationSummary);
    }
    
    private YearlyCalculationSummarySimplified convertToSimplified(Locale locale, YearlyCalculationSummary calculationSummary) {
        List<PeriodProfitSimplified> uniqueMonthlyProfits = new ArrayList<>();
        for (PeriodProfit periodProfit : calculationSummary.getCalculationStatistics().getUniqueMonthlyProfits()) {
            uniqueMonthlyProfits.add(PeriodProfitSimplified.builder()
                    .profit(formatMoney(locale, periodProfit.getProfit()))
                    .startMonth(periodProfit.getStartMonth())
                    .endMonth(periodProfit.getEndMonth())
                    .build()
            );
        }
        
        CalculationStatisticsSimplified calculationStatisticsSimplified = CalculationStatisticsSimplified.builder()
                .averageMonthlyProfit(formatMoney(locale, calculationSummary.getCalculationStatistics().getAverageMonthlyProfit()))
                .uniqueMonthlyProfits(uniqueMonthlyProfits)
                .build();
        
        List<MonthlyCalculationSummarySimplified> monthlySummaries = new ArrayList<>();
        for (MonthlyCalculationSummary monthlyCalculationSummary : calculationSummary.getMonthlySummaries()) {
            monthlySummaries.add(MonthlyCalculationSummarySimplified.builder()
                    .month(monthlyCalculationSummary.getMonth())
                    .summary(formatSummary(locale, monthlyCalculationSummary.getSummary()))
                    .summaryCumulative(formatSummary(locale, monthlyCalculationSummary.getSummaryCumulative()))
                    .build()
            );
        }
        
        return YearlyCalculationSummarySimplified.builder()
                .calculationStatistics(calculationStatisticsSimplified)
                .monthlySummaries(monthlySummaries)
                .build();
    }
    
    private CalculationSummarySimplified formatSummary(Locale locale, CalculationSummary summary) {
        return CalculationSummarySimplified.builder()
                .zus(ZUSTaxSimplified.builder()
                        .socialInsurance(formatMoney(locale, summary.getZus().getSocialInsurance()))
                        .healthInsurance(formatMoney(locale, summary.getZus().getHealthInsurance()))
                        .healthInsuranceContributionToDeduct(formatMoney(locale, summary.getZus().getHealthInsuranceContributionToDeduct()))
                        .workFund(formatMoney(locale, summary.getZus().getWorkFund()))
                        .total(formatMoney(locale, summary.getZus().getTotal()))
                        .contributionToDeductFromIncome(formatMoney(locale, summary.getZus().getContributionToDeductFromIncome()))
                        .build()
                )
                .scaleTaxThresholdReached(summary.isScaleTaxThresholdReached())
                .vat(formatMoney(locale, summary.getVat()))
                .totalInvoiceSum(formatMoney(locale, summary.getTotalInvoiceSum()))
                .incomeToTax(formatMoney(locale, summary.getIncomeToTax()))
                .tax(formatMoney(locale, summary.getTax()))
                .advancePaymentPIT(formatMoney(locale, summary.getAdvancePaymentPIT()))
                .profit(formatMoney(locale, summary.getProfit()))
                .netInvoiceSum(formatMoney(locale, summary.getNetInvoiceSum()))
                .revenueCost(formatMoney(locale, summary.getRevenueCost()))
                .build();
    }
    
    private String formatMoney(Locale locale, Money money) {
        MoneyFormatter moneyFormatter = new MoneyFormatterBuilder()
                .appendAmountLocalized()
                .appendLiteral(" ")
                .appendCurrencySymbolLocalized()
                .toFormatter(locale);
        
        return moneyFormatter.print(money);
    }
}
