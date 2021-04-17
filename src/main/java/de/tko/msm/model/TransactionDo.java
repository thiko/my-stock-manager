package de.tko.msm.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDo {

    String issuer;
    StockDo stockDo;
    TransactionTypeDo transactionTypeDo;
    BigDecimal pricePerShare;
    int amountOfShares;
    LocalDateTime issuedAt;


}
