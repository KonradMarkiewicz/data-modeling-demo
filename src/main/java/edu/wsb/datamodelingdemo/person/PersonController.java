package edu.wsb.datamodelingdemo.person;

import edu.wsb.datamodelingdemo.company.Company;
import edu.wsb.datamodelingdemo.company.CompanyRepository;
import edu.wsb.datamodelingdemo.security.Authority;
import edu.wsb.datamodelingdemo.security.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public PersonController(PersonRepository personRepository, CompanyRepository companyRepository, AuthorityRepository authorityRepository) {
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/list")
    public Iterable<Person> list() {
        return personRepository.findAll();
    }

    @PostMapping("/save")
    public Person save(@RequestParam String username, @RequestParam String password) {
        Person person = new Person(username, password, true);

        return personRepository.save(person);
    }

    @PostMapping("/saveWithCompany")
    public Person saveWithCompany(@RequestParam String username, @RequestParam String password, @RequestParam String companyName) {
        Company company = new Company(companyName);
        companyRepository.save(company);

        Person person = new Person(username, password, true);
        person.setCompany(company);
        return personRepository.save(person);
    }

    @PostMapping("/disable")
    public Optional<Person> disable(@RequestParam String username) {
        Optional<Person> person = personRepository.findByUsernameAndEnabled(username, true);
        person.ifPresent((value) -> {
            value.setEnabled(false);
            personRepository.save(value);
        });
        return person;
    }

    @GetMapping("/show")
    public Optional<Person> show(@RequestParam String username) {
        return personRepository.findByUsername(username);
    }

    @GetMapping("findByFirstNameOrLastName")
    public Optional<Person> findByFirstNameOrLastName(@RequestParam String firstName, @RequestParam String lastname) {
        return personRepository.findByFirstNameOrLastName(firstName, lastname);
    }

    @GetMapping("findByFirstNameNotNull")
    public Iterable<Person> findByFirstNameNotNull() {
        return personRepository.findAllByFirstNameIsNotNull();
    }

    @GetMapping("findAllCreatedAfter")
    public Iterable<Person> findAllCreatedAfter(@RequestParam String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

        return personRepository.findAllEnabledCreatedAfter(sdf.parse(dateString));
    }

    @Transactional
    @PostMapping("/updateLastName")
    public void updateLastName(@RequestParam String username, @RequestParam String newLastName) {
        personRepository.updateLastName(username, newLastName);
    }

    @GetMapping("{username}/authorities")
    public Iterable<Authority> getAuthorities(@PathVariable String username) {
        return authorityRepository.findAllByPersonUsername(username);
    }

    @PostMapping("{username}/authorities")
    public Person addAuthority(@PathVariable String username, @RequestParam String authorityName) {
        Optional<Person> optPerson = personRepository.findByUsernameAndEnabled(username, true);
        if (optPerson.isEmpty()) {
            throw new InvalidParameterException("No user found");
        }

        Optional<Authority> optAuthority = authorityRepository.findByName(authorityName);
        if (optAuthority.isEmpty()) {
            throw new InvalidParameterException("No authority found");
        }

        Person person = optPerson.get();
        Authority authority = optAuthority.get();

        person.getAuthorities().add(authority);
        personRepository.save(person);

        return person;
    }

    @PostMapping("{username}/authorities/delete")
    public Person deleteAuthority(@PathVariable String username, @RequestParam String authorityName) {
        Optional<Person> optPerson = personRepository.findByUsernameAndEnabled(username, true);
        if (optPerson.isEmpty()) {
            throw new InvalidParameterException("No user found");
        }

        Optional<Authority> optAuthority = authorityRepository.findByName(authorityName);
        if (optAuthority.isEmpty()) {
            throw new InvalidParameterException("No authority found");
        }

        Person person = optPerson.get();
        Authority authority = optAuthority.get();

        person.getAuthorities().remove(authority);
        personRepository.save(person);

        return person;
    }

}
