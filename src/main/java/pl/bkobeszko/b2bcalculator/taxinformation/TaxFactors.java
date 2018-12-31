package pl.bkobeszko.b2bcalculator.taxinformation;

import lombok.Builder;
import lombok.Data;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.ZUSTaxRule;
import pl.bkobeszko.b2bcalculator.taxinformation.zus.ZUSTaxFactors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class TaxFactors {
    private Double vatRate;
    private Double linearIncomeTaxRate;
    private Double scaleIncomeTaxRate1;
    private Double scaleIncomeTaxRate2;
    private Money scaleIncomeTaxThreshold;
    private Money minimumSalary;
    private Money estimatedAvgMonthlySalary;
    private Money avgMonthlySalaryWithGainLastQt;
    private ZUSTaxRule zusTaxRule;
    private Double socialBasisFactorNormalZUS;
    private Double socialBasisFactorPreferentialZUS;
    private ZUSTaxFactors normalZUS;
    private ZUSTaxFactors preferentialZUS;
}
