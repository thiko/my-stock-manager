package de.tko.msm.web;

import de.tko.msm.service.StockService;
import de.tko.msm.web.api.Stock;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
@Path("/stocks")
@Slf4j
public class StockResource {

    @Inject
    private StockService stockService;


    @GET
    public Set<Stock> getStocks() {
        return stockService.getAll()
                .stream()
                .map(stockDo ->
                        Stock.builder()
                                .isin(stockDo.getIsin())
                                .wkn(stockDo.getWkn())
                                .name(stockDo.getName())
                                .customName(stockDo.getCustomName())
                                .build())
                .collect(Collectors.toSet());
    }

    @GET
    @Path("{wknOrIsin}")
    public Stock getStock(@PathParam("wknOrIsin") String wknOrIsin) {
        val stockDo = stockService.getStockByWknOrIsin(wknOrIsin);

        return Stock.builder()
                .isin(stockDo.getIsin())
                .wkn(stockDo.getWkn())
                .name(stockDo.getName())
                .customName(stockDo.getCustomName())
                .build();
    }
}
