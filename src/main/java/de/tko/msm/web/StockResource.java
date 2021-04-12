package de.tko.msm.web;

import de.tko.msm.model.Stock;
import de.tko.msm.service.StockService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/stocks")
@Slf4j
public class StockResource {

    @Inject
    private StockService stockService;

    @GET
    public List<Stock> getStocks() {
        return stockService.getAll();
    }
}
