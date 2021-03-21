package edu.wsb.datamodelingdemo.person;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.wsb.datamodelingdemo.company.Company;
import edu.wsb.datamodelingdemo.enumes.State;
import edu.wsb.datamodelingdemo.security.Authority;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("people")
    private Company company;

    @ManyToMany
    @JoinTable(name = "person_authorities",
        joinColumns = @JoinColumn(name = "person_id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities;

    @Column(nullable = false, unique = true, length = 99)
    private String username;

    @Column(length = 100)
    private String firstName;

    private String lastName;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean passwordExpired;

    private Boolean enabled;
    private Date dateCreated;

    @Enumerated(EnumType.STRING)
    private State state = State.PENDING;

    public Person(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.dateCreated = new Date();
    }

    protected Person() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }
}
