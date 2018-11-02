package pl.bkobeszko.b2bcalculator.taxinformation;

import lombok.Builder;
import lombok.Data;
import org.joda.money.Money;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class TaxInformation {
    private Double vatRate;
    private Double linearIncomeTaxRate;
    private Double scaleIncomeTaxRate1;
    private Double scaleIncomeTaxRate2;
    private Money scaleIncomeTaxThreshold;
    private Money zusSocialInsurance;
    private Money zusSocialInsurancePreferential;
    private Money zusHealthInsuranceContributionBasis;
    private Double zusHealthInsuranceContributionBasisRate;
    private Double zusHealthInsuranceContributionToDeductRate;
    private Money zusWorkFund;
    private Money zusWorkFundPreferential;
}
