package pl.bkobeszko.b2bcalculator.taxinformation;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Resources;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Component
@Slf4j
public class TaxInformationStoreFactory {
    private static final String TAX_INFORMATION_FILE = "tax_information.yml";
    private TaxInformationStore taxInformationStore;
    
    public TaxInformationStoreFactory() {
        TaxInformationStore taxInformationStoreLocal = createTaxInformationStore();
        setTaxInformationStore(taxInformationStoreLocal);
    }
    
    public static TaxInformationStore createTaxInformationStore() {
        TaxInformationStore taxInformationStore = null;
        
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            
            try (InputStream stream = Resources.getResource(TAX_INFORMATION_FILE).openStream()) {
                taxInformationStore = mapper.readValue(stream, TaxInformationStore.class);
            }
        } catch (IOException e) {
            log.error("error during reading tax information store: ", e);
        }
        
        return taxInformationStore;
    }
}
