package pl.bkobeszko.b2bcalculator.calculator.zus;

import lombok.Builder;
import lombok.Data;
import org.joda.money.Money;
import pl.bkobeszko.b2bcalculator.calculator.CalculatorUtils;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class ZUSTax {
    /**
     * Ubezpieczenie społeczne.
     */
    @Builder.Default
    private Money socialInsurance = CalculatorUtils.getZeroMoney();

    /**
     * Ubezpieczenie zdrowotne.
     */
    @Builder.Default
    private Money healthInsurance = CalculatorUtils.getZeroMoney();

    /**
     * Składka zdrowotna do odliczenia.
     */
    @Builder.Default
    private Money healthInsuranceContributionToDeduct = CalculatorUtils.getZeroMoney();

    /**
     * Fundusz pracy.
     */
    @Builder.Default
    private Money workFund = CalculatorUtils.getZeroMoney();

    @Builder.Default
    private Money total = CalculatorUtils.getZeroMoney();

    /**
     * Składki do odliczenia od dochodu.
     */
    @Builder.Default
    private Money contributionToDeductFromIncome = CalculatorUtils.getZeroMoney();

    public ZUSTax add(ZUSTax zus) {
        ZUSTax added = ZUSTaxFactory.getEmptyZUS();

        added.setSocialInsurance(zus.getSocialInsurance().plus(getSocialInsurance()));
        added.setHealthInsurance(zus.getHealthInsurance().plus(getHealthInsurance()));
        added.setHealthInsuranceContributionToDeduct(zus.getHealthInsuranceContributionToDeduct().plus(getHealthInsuranceContributionToDeduct()));
        added.setWorkFund(zus.getWorkFund().plus(getWorkFund()));
        added.setTotal(zus.getTotal().plus(getTotal()));
        added.setContributionToDeductFromIncome(zus.getContributionToDeductFromIncome().plus(getContributionToDeductFromIncome()));

        return added;
    }
}
