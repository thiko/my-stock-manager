package de.tko.msm.service;

import de.tko.msm.model.StockDo;
import de.tko.msm.persistence.StockRepository;
import de.tko.msm.persistence.entity.StockEntity;
import de.tko.msm.service.exception.ScrapingException;
import de.tko.msm.service.exception.StockNotFoundException;
import de.tko.msm.service.scraping.StockScrapingWorker;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class StockService {

    @Inject
    private StockScrapingWorker scrapingWorker;

    @Inject
    private StockRepository stockRepository;

    public List<StockDo> getAll() {

        return stockRepository.getAll().stream().map(this::toDomainObject).collect(Collectors.toList());
    }

    public StockDo getStockByWknOrIsin(String wknOrIsin) {
        try {

            val dbResult = stockRepository.getStockByWknOrIsin(wknOrIsin);
            if (dbResult.isEmpty()) {
                // TODO: Check in DB first. Only scrape if needed (and store it afterwards)
                val scrapedStock = scrapingWorker.scrapeStock(wknOrIsin);
                stockRepository.insertOrReplace(toEntity(scrapedStock));
                // TODO: Exception handling?
                return scrapedStock;
            }
            return toDomainObject(dbResult.get());
        } catch (ScrapingException e) {
            throw new StockNotFoundException(wknOrIsin, e);
        }
    }

    // TODO: Extract to mapper
    private StockDo toDomainObject(StockEntity stockEntity) {
        return StockDo.builder()
                .wkn(stockEntity.getWkn())
                .isin(stockEntity.getIsin())
                .name(stockEntity.getName())
                .customName(stockEntity.getCustomName())
                .build();
    }

    private StockEntity toEntity(StockDo stockDo) {
        return StockEntity.builder()
                .wkn(stockDo.getWkn())
                .isin(stockDo.getIsin())
                .name(stockDo.getName())
                .customName(stockDo.getCustomName())
                .build();
    }
}
