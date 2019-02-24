package pl.bkobeszko.b2bcalculator.calculator.summary;

import lombok.Builder;
import lombok.Data;
import org.joda.money.Money;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class ImportantInfo {
    public enum Type {
        ZUS_COULD_BE_PROPORTIONAL, VAT_IS_REQUIRED
    }
    
    private Type type;
    private Money value;
}
