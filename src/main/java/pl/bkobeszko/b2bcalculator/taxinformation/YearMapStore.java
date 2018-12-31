package pl.bkobeszko.b2bcalculator.taxinformation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2018, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
public abstract class YearMapStore<T> {
    private static final Integer DEFAULT_YEAR = 2018;
    
    public abstract T getTaxFactorsForYear(int year);
    public abstract Set<Integer> getYears();
    
    public List<Integer> getYearsSortedDescending() {
        return getYears()
                .stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
    
    protected String getDataStringByYear(Map<Integer, String> dataMap, int year) {
        Integer keyYear = findNearestKeyYear(dataMap.keySet(), year);
        return dataMap.get(keyYear);
    }
    
    protected Double getDataDoubleByYear(Map<Integer, Double> dataMap, int year) {
        Integer keyYear = findNearestKeyYear(dataMap.keySet(), year);
        return dataMap.get(keyYear);
    }
    
    private Integer findNearestKeyYear(Set<Integer> keySet, int year) {
        // get minimal value from key set
        Integer minYear = keySet
                .stream()
                .min(Integer::compare)
                .orElse(DEFAULT_YEAR);
        
        // get year lower or equal than parameter or minimal year
        return keySet
                .stream()
                .filter(key -> key <= year)
                .max(Integer::compare)
                .orElse(minYear);
    }
    
}
