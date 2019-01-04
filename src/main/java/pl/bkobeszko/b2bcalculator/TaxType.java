package pl.bkobeszko.b2bcalculator;

import pl.bkobeszko.b2bcalculator.utils.EnumUtils;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public enum TaxType {
    LINEAR, SCALE;
    
    public static TaxType parseEnum(String taxType, TaxType defaultTaxType) {
        TaxType result = EnumUtils.getEnumIgnoreCase(TaxType.class, taxType);
        
        if (result == null) {
            result = defaultTaxType;
        }

        return result;
    }
}
