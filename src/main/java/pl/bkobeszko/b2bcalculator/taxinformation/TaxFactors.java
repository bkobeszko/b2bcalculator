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
    private double vatRate;
    private Money vatLimit;
    private double linearIncomeTaxRate;
    private double scaleIncomeTaxRate1;
    private double scaleIncomeTaxRate2;
    private Money scaleIncomeTaxThreshold;
    private Money taxFreeAmount;
    private Money minimumSalary;
    private Money estimatedAvgMonthlySalary;
    private Money avgMonthlySalaryWithGainLastQt;
    private ZUSTaxRule zusTaxRule;
    private double socialBasisFactorNormalZUS;
    private double socialBasisFactorPreferentialZUS;
    private ZUSTaxFactors normalZUS;
    private ZUSTaxFactors preferentialZUS;
}
