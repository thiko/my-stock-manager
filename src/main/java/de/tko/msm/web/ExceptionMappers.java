package de.tko.msm.web;

import de.tko.msm.service.exception.StockNotFoundException;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.core.Response;

public class ExceptionMappers {

    @ServerExceptionMapper
    public Response mapException(StockNotFoundException x) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Unable to find stock with given wkn or isin: " + x.getWknOrIsin())
                .build();
    }
}
