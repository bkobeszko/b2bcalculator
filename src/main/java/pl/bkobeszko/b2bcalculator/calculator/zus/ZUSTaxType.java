package pl.bkobeszko.b2bcalculator.calculator.zus;

import pl.bkobeszko.b2bcalculator.utils.EnumUtils;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public enum ZUSTaxType {
    NORMAL, PREFERENTIAL;
    
    public static ZUSTaxType parseEnum(String zusTaxType, ZUSTaxType defaultZUSTaxType) {
        ZUSTaxType result = EnumUtils.getEnumIgnoreCase(ZUSTaxType.class, zusTaxType);
    
        if (result == null) {
            result = defaultZUSTaxType;
        }
    
        return result;
    }
}