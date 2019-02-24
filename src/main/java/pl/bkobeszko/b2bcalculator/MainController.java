package pl.bkobeszko.b2bcalculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bkobeszko.b2bcalculator.taxinformation.TaxFactorsStoreFactory;

import java.util.List;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Controller
@PropertySource("classpath:build.properties")
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
        return taxFactorsStoreFactory.getTaxFactorsStore().getYearsSortedDescending();
    }
    
    @RequestMapping({ "/", "/index" })
    public String index() {
        return "index";
    }
    
}
