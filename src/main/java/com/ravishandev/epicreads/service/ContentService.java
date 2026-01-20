package com.ravishandev.epicreads.service;

import com.google.gson.JsonObject;
import com.ravishandev.epicreads.entity.Category;
import com.ravishandev.epicreads.entity.City;
import com.ravishandev.epicreads.util.AppUtil;
import com.ravishandev.epicreads.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ContentService {

    public String loadCategories(){
        JsonObject responseObject = new JsonObject();

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        List<Category> categories = hibernateSession.createQuery("FROM Category c", Category.class)
                .getResultList();
        responseObject.add("categories", AppUtil.GSON.toJsonTree(categories));
        return AppUtil.GSON.toJson(responseObject);
    }

    public String loadCities(){
        JsonObject responseJson = new JsonObject();

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        List<City> cities =  hibernateSession.createQuery("FROM City c", City.class).getResultList();
        responseJson.add("cities", AppUtil.GSON.toJsonTree(cities));

        hibernateSession.close();

        return AppUtil.GSON.toJson(responseJson);
    }
}
