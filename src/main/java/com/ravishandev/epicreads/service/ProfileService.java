package com.ravishandev.epicreads.service;

import com.google.gson.JsonObject;
import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.entity.Address;
import com.ravishandev.epicreads.entity.User;
import com.ravishandev.epicreads.util.AppUtil;
import com.ravishandev.epicreads.util.HibernateUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileService {

    public String loadUserProfile(@Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Address userAddress = hibernateSession.createQuery("FROM Address a WHERE a.user=:user", Address.class)
                .setParameter("user", user)
                .getSingleResultOrNull();

        userDTO.setLineOne(userAddress.getLineOne());
        userDTO.setLineTwo(userAddress.getLineTwo());
        userDTO.setCityId(userAddress.getCity().getId());
        userDTO.setCityName(userAddress.getCity().getName());
        userDTO.setPostalCode(userAddress.getPostalCode());
        userDTO.setMobile(userAddress.getMobile());

        LocalDateTime createdAt = user.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMMM");
        String sinceAt = createdAt.format(formatter);
        userDTO.setSinceAt(sinceAt);

        responseObject.add("user", AppUtil.GSON.toJsonTree(userDTO));
        hibernateSession.close();

        return AppUtil.GSON.toJson(responseObject);
    }
}
