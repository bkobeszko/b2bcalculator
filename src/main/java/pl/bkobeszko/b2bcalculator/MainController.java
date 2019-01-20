package pl.bkobeszko.b2bcalculator;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStoreFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Controller
public class MainController {
    
    @Autowired
    private TaxFactorsStoreFactory taxFactorsStoreFactory;
    
    @Value("${app.version}")
    private String applicationVersion;
    
    @Value("${app.taxUpdateDate}")
    private String taxUpdateDate;
    
    @ModelAttribute("applicationVersion")
    public String getApplicationVersion() {
        return applicationVersion;
    }
    
    @ModelAttribute("taxUpdateDate")
    public String getTaxUpdateDate() {
        return taxUpdateDate;
    }
    
    @ModelAttribute("years")
    public List<Integer> years() {
        Integer actualYear = DateTime.now().getYear();
        List<Integer> yearsSortedDescending = taxFactorsStoreFactory.getTaxFactorsStore().getYearsSortedDescending();
        
        List<Integer> years = new ArrayList<>();
        
        if (!yearsSortedDescending.contains(actualYear)) {
            years.add(actualYear);
        }
        
        years.addAll(yearsSortedDescending);
        
        return years;
    }
    
    @RequestMapping({ "/", "/index" })
    public String index() {
        return "index";
    }
    
}
