package pl.bkobeszko.b2bcalculator.responses;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class ZUSTaxSimplified {
    private String socialInsurance;
    private String healthInsurance;
    private String healthInsuranceContributionToDeduct;
    private String workFund;
    private String total;
    private String contributionToDeductFromIncome;
}
