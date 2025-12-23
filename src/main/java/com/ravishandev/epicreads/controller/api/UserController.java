package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.service.UserService;
import com.ravishandev.epicreads.util.AppUtil;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(String jsondata){
        UserDTO userDTO = AppUtil.GSON.fromJson(jsondata, UserDTO.class);
        String responseJson = UserService.createAccount(userDTO);
        return Response.ok().entity(responseJson).build();
    }
}
