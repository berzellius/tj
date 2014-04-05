package com.tajinsurance.service;

import com.tajinsurance.domain.Person;
import com.tajinsurance.dto.PersonAjax;
import com.tajinsurance.exceptions.EntityNotSavedException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berz on 24.03.14.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    @PersistenceContext
    EntityManager entityManager;



    @Override
    public List<Person> getAllPersons() {
        Session session = entityManager.unwrap(Session.class);
        List<Person> ps = session.createCriteria(Person.class).list();
        return ps;
    }

    @Override
    public Person getPersonById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        Person p = (Person) session.get(Person.class, id);
        return p;
    }

    @Override
    public List<PersonAjax> searchPersonsByPerson(Person person) {
        Session session = entityManager.unwrap(Session.class);
        Criteria pc = session.createCriteria(Person.class);

        if (person.getSurname() != null) pc.add(Restrictions.like("surname", person.getSurname() + "%").ignoreCase());
        if (person.getName() != null) pc.add(Restrictions.like("name", person.getName() + "%").ignoreCase());
        if (person.getMiddle() != null) pc.add(Restrictions.like("middle", person.getMiddle() + "%").ignoreCase());

        List<Person> ps = pc.list();
        List<PersonAjax> res = new ArrayList<PersonAjax>();

        for (Person p : ps) {
            res.add(personToAjax(p));
        }

        return res;
    }

    @Override
    public PersonAjax createNewPerson(Person person) throws EntityNotSavedException {
        List<PersonAjax> ps = searchPersonsByPerson(person);
        if (ps.size() == 0) {
            Session session = entityManager.unwrap(Session.class);
            session.save(person);
            session.flush();
            session.refresh(person);
            if (person.getId() == null)
                throw new EntityNotSavedException("не удалось создать объект " + person.toString());
            return personToAjax(person);
        } else return personToAjax(getPersonById(ps.get(0).id));
    }

    @Override
    public Person edit(Person person) {



        try {
            entityManager.merge(person);
            entityManager.flush();

            return entityManager.find(Person.class, person.getId());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return person;

    }

    private Person transform(Person p) {


        return p;
    }

    private PersonAjax personToAjax(Person p) {
        return new PersonAjax(p.getId(), p.toString());
    }
}
