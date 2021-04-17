package de.tko.msm.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class DividendDo {

    StockDo stockDo;
    LocalDate issuedAt;
    BigDecimal money;
}
