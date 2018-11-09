package pl.bkobeszko.b2bcalculator.calculator.zus;

import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxInformation;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ZUSTaxFactory {
    private ZUSTaxFactory() {}
    
    public static ZUSTax getNormalZUS(TaxInformation taxInformation, boolean includeHealthInsurance) {
        return buildZUSTax(taxInformation.getZusSocialInsurance(), taxInformation.getZusWorkFund(), taxInformation, includeHealthInsurance);
    }
    
    public static ZUSTax getPreferentialZUS(TaxInformation taxInformation, boolean includeHealthInsurance) {
        return buildZUSTax(taxInformation.getZusSocialInsurancePreferential(), taxInformation.getZusWorkFundPreferential(), taxInformation, includeHealthInsurance);
    }
    
    public static ZUSTax getEmptyZUS() {
        return ZUSTax.builder().build();
    }
    
    private static ZUSTax buildZUSTax(Money dedicatedSocialInsurance, Money dedicatedWorkFund, TaxInformation taxInformation, boolean includeHealthInsurance) {
        Money healthInsuranceContributionBasis = includeHealthInsurance ? taxInformation.getZusHealthInsuranceContributionBasis() : CalculatorUtils.getZeroMoney();
        Money socialInsurance = dedicatedSocialInsurance;
        Money healthInsurance = CalculatorUtils.multiply(healthInsuranceContributionBasis, taxInformation.getZusHealthInsuranceContributionBasisRate());
        Money workFund = dedicatedWorkFund;
        
        return ZUSTax.builder()
                .socialInsurance(socialInsurance)
                .healthInsurance(healthInsurance)
                .healthInsuranceContributionToDeduct(CalculatorUtils.multiply(healthInsuranceContributionBasis, taxInformation.getZusHealthInsuranceContributionToDeductRate()))
                .workFund(workFund)
                .total(socialInsurance.plus(healthInsurance).plus(workFund))
                .contributionToDeductFromIncome(socialInsurance.plus(workFund))
                .build();
    }
}
