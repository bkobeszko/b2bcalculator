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
    private Double retirementBasisPart;
    private Double disabilityBasisPart;
    private Double diseaseBasisPart;
    private Double accidentBasisPart;
    private Double workFundBasisPart;
    private Double healthBasisFactor;
    private Double healthBasisPart;
    private Double healthBasisPartToDeductFromIncome;
}
