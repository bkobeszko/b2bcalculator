package pl.bkobeszko.b2bcalculator.taxinformation.zus;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class ZUSTaxFactors {
    private double retirementBasisPart;
    private double disabilityBasisPart;
    private double diseaseBasisPart;
    private double accidentBasisPart;
    private double workFundBasisPart;
    private double healthBasisFactor;
    private double healthBasisPart;
    private double healthBasisPartToDeductFromIncome;
}
