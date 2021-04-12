package de.tko.msm.service;

import de.tko.msm.model.Stock;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
@Slf4j
public class StockService {

    public List<Stock> getAll() {
        // TODO: Read from db
        return List.of();
    }
}
