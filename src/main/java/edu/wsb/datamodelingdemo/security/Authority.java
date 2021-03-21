package edu.wsb.datamodelingdemo.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Authority {

    @Id
    @GeneratedValue
    private Long id;

    public String getName() {
        return name;
    }

    @Column(nullable = false, unique = true)
    private String name;

    public Authority() {
    }
}
