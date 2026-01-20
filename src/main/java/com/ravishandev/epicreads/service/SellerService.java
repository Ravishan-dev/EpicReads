package com.ravishandev.epicreads.service;

import com.google.gson.JsonObject;
import com.ravishandev.epicreads.dto.SellerDTO;
import com.ravishandev.epicreads.entity.Role;
import com.ravishandev.epicreads.entity.Seller;
import com.ravishandev.epicreads.entity.Status;
import com.ravishandev.epicreads.entity.User;
import com.ravishandev.epicreads.util.AppUtil;
import com.ravishandev.epicreads.util.HibernateUtil;
import com.ravishandev.epicreads.validation.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Cookie;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SellerService {

    public String login(SellerDTO sellerDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        if (sellerDTO.getCompanyEmail() == null) {
            message = "Company Email Address is Required";
        } else if (sellerDTO.getCompanyEmail().isBlank()) {
            message = "Company Email can not be Empty";
        } else if (!sellerDTO.getCompanyEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please Provide Valid Email Address";
        } else if (sellerDTO.getPassword() == null) {
            message = "Password Is Required";
        } else if (sellerDTO.getPassword().isBlank()) {
            message = "Password can not be Empty";
        } else if (!sellerDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please Provide a Valid Password";
        } else {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            Seller dbSeller = hibernateSession.createQuery("FROM Seller s WHERE s.user=:user", Seller.class)
                    .setParameter("user", user)
                    .getSingleResultOrNull();

            if (dbSeller == null) {
                message = "Account Not Found";
            } else {
                if (!dbSeller.getCompanyEmail().equals(sellerDTO.getCompanyEmail())) {
                    message = "Invalid Credentials Please Try Again";
                } else if (!dbSeller.getPassword().equals(sellerDTO.getPassword())) {
                    message = "Invalid Credentials Please Try Again";
                } else {
                    session.setAttribute("seller", dbSeller);
                    status = true;
                    message = "Login Success";
                }
            }
        }
        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public String createAccount(SellerDTO sellerDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        String message = "";
        boolean status = false;

        if (sellerDTO.getCompanyName() == null) {
            message = "Company Name is Required";
        } else if (sellerDTO.getCompanyName().isBlank()) {
            message = "Company Name can not Be Empty";
        } else if (sellerDTO.getCompanyEmail() == null) {
            message = "Company Email Address Is Required";
        } else if (sellerDTO.getCompanyEmail().isBlank()) {
            message = "Company Email Address can not be Empty";
        } else if (!sellerDTO.getCompanyEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please Provide a Valid Email Address";
        } else if (sellerDTO.getCompanyMobile() == null) {
            message = "Mobile Number is Required";
        } else if (sellerDTO.getCompanyMobile().isBlank()) {
            message = "Mobile Number can not be Empty";
        } else if (sellerDTO.getPassword() == null) {
            message = "Password Is Required";
        } else if (sellerDTO.getPassword().isBlank()) {
            message = "Password can not be Empty";
        } else if (!sellerDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide valid password. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (sellerDTO.getConfirmPassword() == null) {
            message = "Confirm Password Is Required";
        } else if (sellerDTO.getConfirmPassword().isBlank()) {
            message = "Confirm Password can not be Empty";
        } else if (!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword())) {
            message = "Confirm Password Does not Match";
        } else if (!sellerDTO.isAgreed()) {
            message = "Please Read And Agreed The Terms And Conditions";
        } else {
            HttpSession session = request.getSession(false);
            User user = (User) session.getAttribute("user");
            if (session == null || user == null) {
                message = "Please Login First";
            } else {
                Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
                Seller dbseller = hibernateSession.createNamedQuery("Seller.getByUser", Seller.class)
                        .setParameter("user", user)
                        .getSingleResultOrNull();

                if (dbseller != null) {
                    message = "Already Have Seller Account Please Login";
                } else {
                    Seller seller = new Seller();
                    seller.setCompanyName(sellerDTO.getCompanyName());
                    seller.setCompanyEmail(sellerDTO.getCompanyEmail());
                    seller.setCompanyMobile(sellerDTO.getCompanyMobile());
                    seller.setPassword(sellerDTO.getConfirmPassword());
                    seller.setUser(user);

                    Status verifiedStatus = hibernateSession.createNamedQuery("Status.findByValue", Status.class)
                            .setParameter("value", String.valueOf(Status.type.VERIFIED))
                            .getSingleResultOrNull();

                    Role sellerRole = hibernateSession.createQuery("FROM Role r WHERE r.value=:value", Role.class)
                            .setParameter("value", String.valueOf(Role.role.SELLER))
                            .getSingleResultOrNull();

                    seller.setStatus(verifiedStatus);
                    seller.setRole(sellerRole);
                    Transaction transaction = hibernateSession.beginTransaction();

                    try {
                        hibernateSession.merge(seller);
                        transaction.commit();
                        status = true;
                        message = "Account Created Successfully!!";
                    } catch (HibernateException e) {
                        transaction.rollback();
                        message = "Account Creation Failed";
                    }
                }
            }
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);

        return AppUtil.GSON.toJson(responseObject);
    }
}
