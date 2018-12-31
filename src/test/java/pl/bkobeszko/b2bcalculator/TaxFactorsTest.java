package pl.bkobeszko.b2bcalculator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.bkobeszko.b2bcalculator.taxinformation.*;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public class TaxFactorsTest {
    public static TaxFactorsStore taxFactorsStore;
    
    @BeforeClass
    public static void beforeClassSetUp() {
        taxFactorsStore = TaxFactorsStoreFactory.createTaxFactorsStore();
    }
    
    @Test
    public void testForYear2017() {
        TaxFactors taxFactorsForYear = taxFactorsStore.getTaxFactorsForYear(2017);
    
        Double expectedVat = 0.23d;
        Double expectedLinearIncomeTax = 0.19d;

        assertThat(taxFactorsForYear.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxFactorsForYear.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testForYear1995() {
        TaxFactors taxFactors = taxFactorsStore.getTaxFactorsForYear(1995);
        
        Double expectedVat = 0.20d;
        Double expectedLinearIncomeTax = 0.13d;
        
        assertThat(taxFactors.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxFactors.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testForYear1900() {
        TaxFactors taxFactors = taxFactorsStore.getTaxFactorsForYear(1900);
        
        Double expectedVat = 0.10d;
        Double expectedLinearIncomeTax = 0.25d;
        
        assertThat(taxFactors.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxFactors.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testForYearZero() {
        TaxFactors taxFactors = taxFactorsStore.getTaxFactorsForYear(0);
        
        Double expectedVat = 0.10d;
        Double expectedLinearIncomeTax = 0.25d;
        
        assertThat(taxFactors.getVatRate()).isEqualTo(expectedVat);
        assertThat(taxFactors.getLinearIncomeTaxRate()).isEqualTo(expectedLinearIncomeTax);
    }
    
    @Test
    public void testGetYears() {
        Set<Integer> expectedYears = Sets.newHashSet(2016, 2019, 2000, 1990, 2017, 2009, 2010, 1995, 2018, 2012, 2013, 2015);
        
        assertThat(taxFactorsStore.getYears()).isEqualTo(expectedYears);
    }
    
    @Test
    public void testGetYearsSortedDescending() {
        List<Integer> expectedYears = Lists.newArrayList(2019, 2018, 2017, 2016, 2015, 2013, 2012, 2010, 2009, 2000, 1995, 1990);
        
        assertThat(taxFactorsStore.getYearsSortedDescending()).isEqualTo(expectedYears);
    }
}
