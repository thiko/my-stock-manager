package de.tko.msm.service;

import de.tko.msm.model.StockDo;
import de.tko.msm.service.exception.ScrapingException;
import de.tko.msm.service.exception.StockNotFoundException;
import de.tko.msm.service.scraping.StockScrapingWorker;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Set;

@ApplicationScoped
@Slf4j
public class StockService {

    @Inject
    private StockScrapingWorker scrapingWorker;

    public Set<StockDo> getAll() {
        try {
            return Set.of(scrapingWorker.scrapeStock("A0TGJ5"));
        } catch (ScrapingException e) {
            e.printStackTrace();
        }
        return Set.of();
    }

    public StockDo getStockByWknOrIsin(String wknOrIsin) {
        try {
            // TODO: Check in DB first. Only scrape if needed (and store it afterwards)
            return scrapingWorker.scrapeStock(wknOrIsin);
        } catch (ScrapingException e) {
            throw new StockNotFoundException(wknOrIsin, e);
        }
    }
}
