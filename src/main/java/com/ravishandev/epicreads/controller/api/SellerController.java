package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.dto.SellerDTO;
import com.ravishandev.epicreads.service.SellerService;
import com.ravishandev.epicreads.util.AppUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/sellers")
public class SellerController {

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(String jsonData, @Context HttpServletRequest request) {
        SellerDTO sellerDTO = AppUtil.GSON.fromJson(jsonData, SellerDTO.class);
        String responseJson = new SellerService().login(sellerDTO, request);
        return Response.ok().entity(responseJson).build();
    }

    @Path("/create-account")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(String jsonData, @Context HttpServletRequest request) {
        SellerDTO sellerDTO = AppUtil.GSON.fromJson(jsonData, SellerDTO.class);
        String responseJson = new SellerService().createAccount(sellerDTO, request);
        return Response.ok().entity(responseJson).build();
    }
}
