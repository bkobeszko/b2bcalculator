package pl.bkobeszko.b2bcalculator.calculator;

import lombok.Getter;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class CalculatorUtils {

    private static final String CURRENCY_CODE = "PLN";
    private static final CurrencyUnit CURRENCY_UNIT = CurrencyUnit.of(CURRENCY_CODE);
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private CalculatorUtils() {}

    @Getter
    private static final Money halfMoney = CalculatorUtils.getMoneyOf(0.5);

    @Getter
    private static final Money zeroMoney = Money.zero(CURRENCY_UNIT);

    public static Money getMoneyOf(double amount) {
        return Money.of(CURRENCY_UNIT, amount, ROUNDING_MODE);
    }

    public static Money multiply(Money money1, double money2) {
        return money1.multipliedBy(money2, ROUNDING_MODE);
    }

    public static Money multiply(double money1, Money money2) {
        return multiply(money2, money1);
    }

    public static Money divide(Money money1, double money2) {
        return money1.dividedBy(money2, ROUNDING_MODE);
    }

    public static Money divide(double money1, Money money2) {
        return divide(money2, money1);
    }
}
