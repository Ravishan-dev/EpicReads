package com.ravishandev.epicreads.service;

import com.google.gson.JsonObject;
import com.ravishandev.epicreads.dto.UserDTO;
import com.ravishandev.epicreads.entity.Status;
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

public class UserService {

    public String updatePassword(UserDTO userDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        HttpSession session = request.getSession();
        if (session == null) {
            message = "Please Login First";
        } else {
            User sessionUser = (User) session.getAttribute("user");
            if (!sessionUser.getPassword().equals(userDTO.getCurrentPassword())) {
                message = "Current Password does not Match...Please Try Again";
            } else {
                if (userDTO.getNewPassword() == null) {
                    message = "New Password is Required";
                } else if (userDTO.getNewPassword().isBlank()) {
                    message = "New Password can not be Empty";
                } else if (!userDTO.getNewPassword().matches(Validator.PASSWORD_VALIDATION)) {
                    message = "Please Provide a Valid Password";
                } else if (userDTO.getConfirmPassword() == null) {
                    message = "Confirm Password id Required";
                } else if (userDTO.getConfirmPassword().isBlank()) {
                    message = "Confirm Password can not be Empty";
                } else if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
                    message = "New Password and Confirm Password does not Match";
                } else {
                    Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
                    User dbUser = hibernateSession.createNamedQuery("User.getByEmail", User.class)
                            .setParameter("email", sessionUser.getEmail())
                            .getSingleResultOrNull();

                    dbUser.setPassword(userDTO.getConfirmPassword());

                    Transaction transaction = hibernateSession.beginTransaction();
                    try {
                        hibernateSession.persist(dbUser);
                        transaction.commit();
                        status = true;
                        message = "Password Changed Successfully";
                    } catch (HibernateException e) {
                        transaction.rollback();
                        message = "Password Updating Failed";
                    }
                    hibernateSession.close();
                }
            }
        }
        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public String userLogin(UserDTO userDTO, @Context HttpServletRequest request) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        if (userDTO.getEmail() == null) {
            message = "Email Address is Required";
        } else if (userDTO.getEmail().isBlank()) {
            message = "Email Address Can not be Empty";
        } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please Provide a valid Email";
        } else if (userDTO.getPassword() == null) {
            message = "Password is Required";
        } else if (userDTO.getPassword().isBlank()) {
            message = "Password can not be Empty";
        } else {
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            User singleUser = hibernateSession.createNamedQuery("User.getByEmail", User.class)
                    .setParameter("email", userDTO.getEmail())
                    .getSingleResultOrNull();

            if (singleUser == null) {
                message = "Account not found Please Register First";
            } else {
                if (!singleUser.getPassword().equals(userDTO.getPassword())) {
                    message = "Invalid Credentials... Please Try Again";
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", singleUser);
                    status = true;
                    message = "Login Success!!!";
                }
            }
            hibernateSession.close();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public String createAccount(UserDTO userDTO) {

        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        if (userDTO.getFirstName() == null) {
            message = "First Name is Required";
        } else if (userDTO.getFirstName().isBlank()) {
            message = "Fist Name can not be Empty";
        } else if (userDTO.getLastName() == null) {
            message = "Last Name id Required";
        } else if (userDTO.getLastName().isBlank()) {
            message = "Last Name can not be Empty";
        } else if (userDTO.getEmail() == null) {
            message = "Email is Required";
        } else if (userDTO.getEmail().isBlank()) {
            message = "Email can not be Empty";
        } else if (!userDTO.getEmail().matches(Validator.EMAIL_VALIDATION)) {
            message = "Please Provide a Valid Email Address";
        } else if (userDTO.getPassword() == null) {
            message = "Password is Required";
        } else if (userDTO.getPassword().isBlank()) {
            message = "Password can not be Empty";
        } else if (!userDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide valid password. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else {
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            User singleUser = hibernateSession.createNamedQuery("User.getByEmail", User.class)
                    .setParameter("email", userDTO.getEmail())
                    .getSingleResultOrNull();
            if (singleUser != null) {
                message = "This Email Already Registered... Please Enter Another Email Address";
            } else {
                User user = new User();
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setPassword(userDTO.getPassword());

                Status pendingStatus = hibernateSession.createNamedQuery("Status.findByValue", Status.class)
                        .setParameter("value", String.valueOf(Status.type.PENDING))
                        .getSingleResultOrNull();

                user.setStatus(pendingStatus);

                Transaction transaction = hibernateSession.beginTransaction();
                try {
                    hibernateSession.persist(user);
                    transaction.commit();
                    status = true;
                    message = "Account Creation Successful";
                } catch (HibernateException e) {
                    transaction.rollback();
                    message = "Account Creation Failed...Please Try Again";
                }
            }
            hibernateSession.close();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }
}
