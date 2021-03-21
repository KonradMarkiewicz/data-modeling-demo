package edu.wsb.datamodelingdemo.person;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person,Long> {

    Optional<Person> findByUsername(String username);

    Optional<Person> findByFirstNameOrLastName(String firstName, String lastName);

    Iterable<Person> findAllByFirstNameIsNotNull();

    @Query("select p from Person p where p.dateCreated >= : date order by p.id desc")
    Iterable<Person> findAllEnabledCreatedAfter(@Param("date") Date date);

    Optional<Person> findByUsernameAndEnabled(String username, boolean enabled);

    @Modifying
    @Query("update Person set lastName = :lastName where username = :username")
    void updateLastName(@Param("username") String username, @Param("lastName") String lastName);

}
