package com.ravishandev.epicreads.service;

import com.google.gson.JsonObject;
import com.ravishandev.epicreads.dto.BookDTO;
import com.ravishandev.epicreads.entity.*;
import com.ravishandev.epicreads.util.AppUtil;
import com.ravishandev.epicreads.util.HibernateUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BookService {

    public String addBook(BookDTO bookDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        if (bookDTO.getTittle() == null) {
            message = "Tittle is Required";
        } else if (bookDTO.getTittle().isBlank()) {
            message = "Tittle can nor be Null";
        } else if (bookDTO.getIsbn() == null) {
            message = "ISBN is Required";
        } else if (bookDTO.getIsbn().isBlank()) {
            message = "ISBN can not be Empty";
        } else if (bookDTO.getDescription() == null) {
            message = "Description is Required";
        } else if (bookDTO.getDescription().isBlank()) {
            message = "Description can not be Empty";
        } else if (bookDTO.getAuthor() == null) {
            message = "Author id Required";
        } else if (bookDTO.getAuthor().isBlank()) {
            message = "Author can not be Empty";
        } else if (bookDTO.getPrice() == null) {
            message = "Price is Required";
        } else if (bookDTO.getPrice().isBlank()) {
            message = "Price can not be Empty";
        } else if (bookDTO.getQty() == null) {
            message = "Quantity is Required";
        } else if (bookDTO.getQty().isBlank()) {
            message = "Quantity can not be Empty";
        } else {
            HttpSession session = request.getSession(false);
            Seller seller = (Seller) session.getAttribute("seller");
            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

            Category category = hibernateSession.createQuery("FROM Category c WHERE c.id=:id", Category.class)
                    .setParameter("id", bookDTO.getCategoryId())
                    .getSingleResultOrNull();
            if(category == null){
                message = "Invalid Category Selected";
            }

            Status inStock = hibernateSession.createNamedQuery("Status.findByValue", Status.class)
                    .setParameter("value", String.valueOf(Status.type.InStock))
                    .getSingleResultOrNull();

            Book book = new Book();
            book.setTittle(bookDTO.getTittle());
            book.setIsbn(bookDTO.getIsbn());
            book.setDescription(bookDTO.getDescription());
            book.setAuthor(bookDTO.getAuthor());
            book.setPrice(bookDTO.getPrice());
            book.setQty(bookDTO.getQty());
            book.setCategory(category);
            book.setStatus(inStock);
            book.setSeller(seller);

            Stock stock = new Stock();
            stock.setBook(book);
            stock.setQuantity(bookDTO.getQty());
            stock.setPrice(bookDTO.getPrice());
            stock.setStatus(inStock);
            stock.setSeller(seller);

            Transaction transaction = hibernateSession.beginTransaction();
            try {
                hibernateSession.persist(book);
                hibernateSession.persist(stock);
                transaction.commit();
                status = true;
                message = "Book Added Successfully";
            } catch (HibernateException e) {
                transaction.rollback();
                message = e.getMessage();
            }finally {
                hibernateSession.close();
            }
        }
        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }
}
