package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.service.UserService;
import com.ravishandev.epicreads.util.AppUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserController {

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLogin(String jsondata, @Context HttpServletRequest request){
        UserDTO userDTO = AppUtil.GSON.fromJson(jsondata, UserDTO.class);
        String responseJson = new UserService().userLogin(userDTO, request);
        return Response.ok().entity(responseJson).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(String jsondata){
        UserDTO userDTO = AppUtil.GSON.fromJson(jsondata, UserDTO.class);
        String responseJson = new UserService().createAccount(userDTO);
        return Response.ok().entity(responseJson).build();
    }
}
