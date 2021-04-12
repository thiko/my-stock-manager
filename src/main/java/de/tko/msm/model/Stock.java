package de.tko.msm.model;

import lombok.Data;

@Data
public class Stock {
    
    String wkn;
    String isin;
    String name;
    String customName;
}
