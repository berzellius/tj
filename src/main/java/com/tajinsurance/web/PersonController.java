package com.tajinsurance.web;

import com.google.gson.Gson;
import com.tajinsurance.domain.Person;
import com.tajinsurance.dto.PersonAjax;
import com.tajinsurance.dto.PersonSaveAjaxAction;
import com.tajinsurance.exceptions.EntityNotSavedException;
import com.tajinsurance.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * Created by berz on 24.03.14.
 */
@RequestMapping("/persons")
@Controller
@RooWebScaffold(path = "persons", formBackingObject = Person.class)
public class PersonController {

    @Autowired
    PersonService personService;

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        uiModel.addAttribute("person", new Person());
        return "persons/create";
    }



    @RequestMapping(params = "search", method = RequestMethod.POST)
    @ResponseBody byte[] searchPerson(Person person){
        Gson gson = new Gson();
        String g = gson.toJson(personService.searchPersonsByPerson(person));
        try {
            return g.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Это значит, что utf нет.
            return null;
        }
    }

    @RequestMapping(params = "new", method = RequestMethod.POST)
    @ResponseBody byte[] createPerson(Person person){
        Gson gson = new Gson();
        PersonSaveAjaxAction res = new PersonSaveAjaxAction();
        try {
            PersonAjax p = personService.createNewPerson(person);
            res.personAjax = p;
            res.success = true;
            res.message = "success adding";
        } catch (EntityNotSavedException e) {
            res.success = false;
            res.message = e.getMessage();
        }
        String g = gson.toJson(res);
        try {
            return g.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Это значит, что utf нет.
            return null;
        }
    }

}
