package pl.bkobeszko.b2bcalculator.taxinformation;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Resources;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bkobeszko.b2bcalculator.taxinformation.zus.ZUSTaxFactorsStore;

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
public class TaxFactorsStoreFactory {
    private static final String TAX_FACTORS_DIRECTORY = "taxes/";
    private static final String TAX_FACTORS_FILE = TAX_FACTORS_DIRECTORY + "tax_factors.yml";
    private static final String NORMAL_ZUS_TAX_FACTORS_FILE = TAX_FACTORS_DIRECTORY + "zus_normal.yml";
    private static final String PREFERENTIAL_ZUS_TAX_FACTORS_FILE = TAX_FACTORS_DIRECTORY + "zus_preferential.yml";
    private TaxFactorsStore taxFactorsStore;
    
    public TaxFactorsStoreFactory() {
        TaxFactorsStore taxFactorsStoreLocal = createTaxFactorsStore();
        setTaxFactorsStore(taxFactorsStoreLocal);
    }
    
    public static TaxFactorsStore createTaxFactorsStore() {
        TaxFactorsStore taxFactorsStore = null;
        
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    
            taxFactorsStore = readValue(mapper, TAX_FACTORS_FILE, TaxFactorsStore.class);
            taxFactorsStore.setNormalZUSTaxFactorsStore(readValue(mapper, NORMAL_ZUS_TAX_FACTORS_FILE, ZUSTaxFactorsStore.class));
            taxFactorsStore.setPreferentialZUSTaxFactorsStore(readValue(mapper, PREFERENTIAL_ZUS_TAX_FACTORS_FILE, ZUSTaxFactorsStore.class));
        } catch (IOException e) {
            log.error("error during reading tax factors store: ", e);
        }
        
        return taxFactorsStore;
    }
    
    private static <T> T readValue(ObjectMapper mapper, String resourceFileName, Class<T> valueType) throws IOException {
        try (InputStream stream = Resources.getResource(resourceFileName).openStream()) {
            return mapper.readValue(stream, valueType);
        }
    }
}
