package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.service.CityService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/data")
public class ContentController {

    @Path("/cities")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadCities(){
        String responsejson = new CityService().loadCities();
        return Response.ok().entity(responsejson).build();
    }
}
