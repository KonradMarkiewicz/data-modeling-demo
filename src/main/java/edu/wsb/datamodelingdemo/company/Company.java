package edu.wsb.datamodelingdemo.company;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.wsb.datamodelingdemo.person.Person;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Company {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "company")
    @JsonIgnoreProperties("company")
    private Set<Person> people;

    @Column(nullable = false, unique = true)
    private String name;

    private String address;

    public Company(String name) {
        this.name = name;
    }

    public Company() {

    }

    public String getName() {
        return name;
    }

    public Set<Person> getPeople() {
        return people;
    }
}
