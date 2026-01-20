package com.ravishandev.epicreads.controller.api;

import com.ravishandev.epicreads.dto.BookDTO;
import com.ravishandev.epicreads.service.BookService;
import com.ravishandev.epicreads.util.AppUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/books")
public class BookController {

    @Path("/add-books")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBook(String jsonData, @Context HttpServletRequest request) {
        BookDTO bookDTO = AppUtil.GSON.fromJson(jsonData, BookDTO.class);
        String responseJson = new BookService().addBook(bookDTO, request);
        return Response.ok().entity(responseJson).build();
    }
}
