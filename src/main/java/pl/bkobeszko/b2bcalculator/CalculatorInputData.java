package pl.bkobeszko.b2bcalculator;

import lombok.Builder;
import lombok.Data;
import pl.bkobeszko.b2bcalculator.calculator.zus.ZUSTaxType;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class CalculatorInputData {
    private double monthlyNetIncome;
    private double monthlyCosts;
    
    @Builder.Default
    private int year = 2017;
    
    @Builder.Default
    private boolean payZUSHealthInsurance = true;
    
    @Builder.Default
    private boolean payVAT = true;
    
    private TaxType taxType;
    
    @Builder.Default
    private ZUSTaxType zusTaxType = ZUSTaxType.NORMAL;
}
