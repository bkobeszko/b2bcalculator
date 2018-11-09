package pl.bkobeszko.b2bcalculator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxInformation;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxInformationStore;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxInformationStoreFactory;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class TaxInformationTest {
    private static TaxInformationStore taxInformationStore;
    
    @BeforeClass
    public static void beforeClassSetUp() {
        taxInformationStore = TaxInformationStoreFactory.createTaxInformationStore();
    }
    
    @Test
    public void testForYear2017() {
        TaxInformation taxInformation = taxInformationStore.getTaxInformationForYear(2017);
        
        Double expectedVat = 0.23d;
        Double expectedLinearIncomeTax = 0.19d;
        
        assertThat(taxInformation.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxInformation.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testForYear1995() {
        TaxInformation taxInformation = taxInformationStore.getTaxInformationForYear(1995);
        
        Double expectedVat = 0.20d;
        Double expectedLinearIncomeTax = 0.13d;
        
        assertThat(taxInformation.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxInformation.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testForYear1900() {
        TaxInformation taxInformation = taxInformationStore.getTaxInformationForYear(1900);
        
        Double expectedVat = 0.10d;
        Double expectedLinearIncomeTax = 0.25d;
        
        assertThat(taxInformation.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxInformation.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testForYearZero() {
        TaxInformation taxInformation = taxInformationStore.getTaxInformationForYear(0);
        
        Double expectedVat = 0.10d;
        Double expectedLinearIncomeTax = 0.25d;
        
        assertThat(taxInformation.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxInformation.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testGetYears() {
        Set<Integer> expectedYears = Sets.newHashSet(1990, 1995, 2000, 2009);
        
        assertThat(taxInformationStore.getYears()).isEqualTo(expectedYears);
    }
    
    @Test
    public void testGetYearsSortedDescending() {
        List<Integer> expectedYears = Lists.newArrayList(2009, 2000, 1995, 1990);
        
        assertThat(taxInformationStore.getYearsSortedDescending()).isEqualTo(expectedYears);
    }
}
