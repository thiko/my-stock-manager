package de.tko.msm.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Stock {

    String wkn;
    String isin;
    String name;
    String customName;
}
