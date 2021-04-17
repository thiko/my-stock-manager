package de.tko.msm.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class StockDo {

    @EqualsAndHashCode.Include
    String wkn;
    @EqualsAndHashCode.Include
    String isin;
    String name;
    String customName;
}
