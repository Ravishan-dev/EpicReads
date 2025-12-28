package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.service.ProfileService;
import com.ravishandev.epicreads.util.AppUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/profiles")
public class ProfileController {


    @Path("/update-profile")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(String jsondata, @Context HttpServletRequest request) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsondata, UserDTO.class);
        String responseJson = new ProfileService().updateProfile(userDTO, request);
        return Response.ok().entity(responseJson).build();
    }

    @Path("/user-profile")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadUserProfile(@Context HttpServletRequest request) {
        String responseJson = new ProfileService().loadUserProfile(request);
        return Response.ok().entity(responseJson).build();
    }
}
