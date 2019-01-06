package pl.bkobeszko.b2bcalculator.responses;

import lombok.Builder;
import lombok.Data;

/**
 * Copyright (c) 2019, GNU AGPL v. 3.0
 *
 * @author Bartłomiej Kobeszko
 */
@Data
@Builder
public class ImportantInfoSimplified {
    private String type;
    private String value;
}
