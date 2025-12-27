package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/profiles")
public class ProfileController {

    @Path("/user-profile")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadUserProfile(@Context HttpServletRequest request) {
        String responseJson = new ProfileService().loadUserProfile(request);
        return Response.ok().entity(responseJson).build();
    }
}
