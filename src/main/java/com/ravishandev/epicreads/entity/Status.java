package com.ravishandev.epicreads.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String value;

    public enum type{
        ACTIVE,
        INACTIVE,
        PENDING,
        BLOCKED,
        DELIVERED,
        PACKING,
        APPROVED,
        REJECTED,
        CANCELLED,
        VERIFIED,
        RECEIVED,
        COMPLETED
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
