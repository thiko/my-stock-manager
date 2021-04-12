package de.tko.msm.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TransactionTaskPlanner {

    Stock stock;
    BigDecimal money;
    LocalDate validFrom;
    LocalDate validUntil;
    LocalDate nextExecution;
}
