package com.ravishandev.epicreads.service;

import com.google.gson.JsonObject;
import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.entity.Address;
import com.ravishandev.epicreads.entity.City;
import com.ravishandev.epicreads.entity.User;
import com.ravishandev.epicreads.util.AppUtil;
import com.ravishandev.epicreads.util.HibernateUtil;
import com.ravishandev.epicreads.validation.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileService {

    public String updateProfile(UserDTO userDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        if (userDTO.getFirstName() == null) {
            message = "First Name is Required";
        } else if (userDTO.getFirstName().isBlank()) {
            message = "Fist Name can not be Empty";
        } else if (userDTO.getLastName() == null) {
            message = "Last Name is Required";
        } else if (userDTO.getLastName().isBlank()) {
            message = "Last Name can not be Empty";
        } else if (userDTO.getEmail() == null) {
            message = "Email Address is Required";
        } else if (userDTO.getEmail().isBlank()) {
            message = "Email Address can not be Empty";
        } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please Provide a valid Email Address";
        } else if (userDTO.getMobile() == null) {
            message = "Mobile Is Required";
        } else if (userDTO.getMobile().isBlank()) {
            message = "Mobile can not be Empty";
        } else if (!userDTO.getMobile().matches(Validator.MOBILE_VALIDATION)) {
            message = "Please Provide a Valid Mobile";
        } else if (userDTO.getLineOne() == null) {
            message = "Address line one is Required";
        } else if (userDTO.getLineOne().isBlank()) {
            message = "Address Line One can not be Empty";
        } else if (userDTO.getLineTwo() == null) {
            message = "Address Line two id Required";
        } else if (userDTO.getLineTwo().isBlank()) {
            message = "Address Line Two can not be Empty";
        } else if (userDTO.getCityId() == 0) {
            message = "Please Select A City";
        } else if (userDTO.getPostalCode() == null) {
            message = "Postal Code is Required";
        } else if (userDTO.getPostalCode().isBlank()) {
            message = "Postal Code can not be Empty";
        } else {
            HttpSession httpSession = request.getSession(false);
            if (httpSession == null) {
                message = "Please Login First";
            } else if (httpSession.getAttribute("user") == null) {
                message = "Please Login First";
            } else {
                User sessionUser = (User) httpSession.getAttribute("user");
                Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
                User dbUser = hibernateSession.createNamedQuery("User.getByEmail", User.class)
                        .setParameter("email", sessionUser.getEmail())
                        .getSingleResultOrNull();
                dbUser.setFirstName(userDTO.getFirstName());
                dbUser.setLastName(userDTO.getLastName());
                dbUser.setEmail(userDTO.getEmail());

                Address userAddress = hibernateSession.createQuery("FROM Address a WHERE a.user=:user", Address.class)
                        .setParameter("user", sessionUser)
                        .getSingleResultOrNull();

                if (userAddress != null) {
                    userAddress.setLineOne(userDTO.getLineOne());
                    userAddress.setLineTwo(userDTO.getLineTwo());
                    userAddress.setMobile(userDTO.getMobile());

                    City city = hibernateSession.find(City.class, userDTO.getCityId());

                    if (city != null) {
                        userAddress.setCity(city);
                    }

                    userAddress.setPostalCode(userDTO.getPostalCode());
                }

                Transaction transaction = hibernateSession.beginTransaction();
                try {
                    hibernateSession.merge(dbUser);
                    hibernateSession.merge(userAddress);
                    transaction.commit();
                    httpSession.setAttribute("user", dbUser);
                    status = true;
                    message = "Profile Details Update Succes";
                } catch (HibernateException e) {
                    transaction.rollback();
                    message = "Profile Updating Failed";
                }
                hibernateSession.close();
            }
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

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
