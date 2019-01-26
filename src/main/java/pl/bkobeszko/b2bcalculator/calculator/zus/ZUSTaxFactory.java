package pl.bkobeszko.b2bcalculator.calculator.zus;

import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactors;
import pl.bkobeszko.b2bcalculator.taxinformation.zus.ZUSTaxFactors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class ZUSTaxFactory {
    private ZUSTaxFactory() {}
    
    public static ZUSTax getNormalZUS(TaxFactors taxFactors, boolean includeHealthInsurance) {
        ZUSTaxFactors zusTaxFactors = taxFactors.getNormalZUS();
        Money basis = taxFactors.getEstimatedAvgMonthlySalary();
        Double basisFactor = taxFactors.getSocialBasisFactorNormalZUS();
        
        return buildZUSTax(zusTaxFactors, basis, basisFactor, taxFactors.getAvgMonthlySalaryWithGainLastQt(), includeHealthInsurance);
    }
    
    public static ZUSTax getPreferentialZUS(TaxFactors taxFactors, boolean includeHealthInsurance) {
        ZUSTaxFactors zusTaxFactors = taxFactors.getPreferentialZUS();
        Money basis = taxFactors.getMinimumSalary();
        Double basisFactor = taxFactors.getSocialBasisFactorPreferentialZUS();
    
        return buildZUSTax(zusTaxFactors, basis, basisFactor, taxFactors.getAvgMonthlySalaryWithGainLastQt(), includeHealthInsurance);
    }
    
    public static ZUSTax getProportionalZUS(TaxFactors taxFactors, Money basis, boolean includeHealthInsurance) {
        ZUSTaxFactors zusTaxFactors = taxFactors.getNormalZUS();
        Double basisFactor = 1d;
    
        return buildZUSTax(zusTaxFactors, basis, basisFactor, taxFactors.getAvgMonthlySalaryWithGainLastQt(), includeHealthInsurance);
    }
    
    public static ZUSTax getEmptyZUS() {
        return ZUSTax.builder().build();
    }
    
    private static ZUSTax buildZUSTax(ZUSTaxFactors zusTaxFactors, Money basis, Double basisFactor, Money healthBasisIndicator, boolean includeHealthInsurance) {
        Money basisPart = CalculatorUtils.multiply(basis, basisFactor);
        
        // four ingredients of social contribution
        Money retirementContribution = CalculatorUtils.multiply(basisPart, zusTaxFactors.getRetirementBasisPart());
        Money disabilityContribution = CalculatorUtils.multiply(basisPart, zusTaxFactors.getDisabilityBasisPart());
        Money diseaseContribution = CalculatorUtils.multiply(basisPart, zusTaxFactors.getDiseaseBasisPart());
        Money accidentContribution = CalculatorUtils.multiply(basisPart, zusTaxFactors.getAccidentBasisPart());
        Money socialInsurance = retirementContribution
                .plus(disabilityContribution)
                .plus(diseaseContribution)
                .plus(accidentContribution);
    
        Money workFund = CalculatorUtils.multiply(basisPart, zusTaxFactors.getWorkFundBasisPart());
    
        // a temporary hack:)
        if (!includeHealthInsurance) {
            healthBasisIndicator = CalculatorUtils.getZeroMoney();
        }
        
        Money healthBasis = CalculatorUtils.multiply(healthBasisIndicator, zusTaxFactors.getHealthBasisFactor());
        Money healthInsurance = CalculatorUtils.multiply(healthBasis, zusTaxFactors.getHealthBasisPart());
        Money healthInsuranceContributionToDeduct = CalculatorUtils.multiply(healthBasis, zusTaxFactors.getHealthBasisPartToDeductFromIncome());
        
        Money total = socialInsurance
                .plus(healthInsurance)
                .plus(workFund);
        
        Money contributionToDeductFromIncome = socialInsurance
                .plus(workFund);
    
        return ZUSTax.builder()
                .socialInsurance(socialInsurance)
                .healthInsurance(healthInsurance)
                .healthInsuranceContributionToDeduct(healthInsuranceContributionToDeduct)
                .workFund(workFund)
                .total(total)
                .contributionToDeductFromIncome(contributionToDeductFromIncome)
                .build();
    }
}
