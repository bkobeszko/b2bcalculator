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
        ZUSCouldBeProportional, VATIsRequired
    }
    
    private Type type;
    private Money value;
}
