package de.tko.msm.service.exception;

import lombok.Getter;

@Getter
public class StockNotFoundException extends RuntimeException {

    private final String wknOrIsin;

    public StockNotFoundException(String wknOrIsin, Throwable cause) {
        super("Unable to find stock by given wkn or isin: " + wknOrIsin, cause);
        this.wknOrIsin = wknOrIsin;
    }
}
