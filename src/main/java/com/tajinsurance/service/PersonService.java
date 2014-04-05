package com.tajinsurance.service;

import com.tajinsurance.domain.Person;
import com.tajinsurance.dto.PersonAjax;
import com.tajinsurance.exceptions.EntityNotSavedException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by berz on 24.03.14.
 */
@Service
public interface PersonService {
    public List<Person> getAllPersons();

    public Person getPersonById(Long id);

    public List<PersonAjax> searchPersonsByPerson(Person person);

    public PersonAjax createNewPerson(Person person)  throws EntityNotSavedException;

    public Person edit(Person person);
}
