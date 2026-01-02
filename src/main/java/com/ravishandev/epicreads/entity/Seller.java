package com.ravishandev.epicreads.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_email", nullable = false)
    private String companyEmail;

    @Column(name = "company_mobile", nullable = false)
    private String companyMobile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    private List<Book> books;
}
