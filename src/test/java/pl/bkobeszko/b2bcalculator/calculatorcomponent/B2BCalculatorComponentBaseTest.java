package pl.bkobeszko.b2bcalculator.calculatorcomponent;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pl.bkobeszko.b2bcalculator.B2BCalculatorComponent;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStore;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStoreFactory;

import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public abstract class B2BCalculatorComponentBaseTest {
    static TaxFactorsStore taxFactorsStore;
    
    @Mock
    private TaxFactorsStoreFactory taxFactorsStoreFactory;
    
    @InjectMocks
    protected B2BCalculatorComponent b2bCalculatorComponent;
    
    @BeforeClass
    public static void beforeClassSetUp() {
        taxFactorsStore = TaxFactorsStoreFactory.createTaxFactorsStore();
    }
    
    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(taxFactorsStoreFactory.getTaxFactorsStore()).thenReturn(taxFactorsStore);
    }
}