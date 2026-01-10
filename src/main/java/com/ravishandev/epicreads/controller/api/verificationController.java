package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.service.UserService;
import com.ravishandev.epicreads.util.AppUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/verify-accounts")
public class verificationController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyAccount(String jsonData){
        UserDTO userDto = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        String responseJson = new UserService().verifyAccount(userDto);
        return Response.ok().entity(responseJson).build();
    }
}
