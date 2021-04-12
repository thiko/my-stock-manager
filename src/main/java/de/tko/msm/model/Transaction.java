package de.tko.msm.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {

    String issuer;
    Stock stock;
    TransactionType transactionType;
    BigDecimal pricePerShare;
    int amountOfShares;
    LocalDateTime issuedAt;


}
