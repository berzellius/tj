package com.tajinsurance.web;

import com.google.gson.Gson;
import com.tajinsurance.domain.Person;
import com.tajinsurance.dto.PersonAjax;
import com.tajinsurance.dto.PersonSaveAjaxAction;
import com.tajinsurance.dto.PersonUpdateAjaxAction;
import com.tajinsurance.exceptions.EntityNotSavedException;
import com.tajinsurance.exceptions.ResourceNotFoundException;
import com.tajinsurance.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @RequestMapping(value = "/edit", produces = "text/html")
    public String editPersonForm(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "ajax", required = false) String ajax,
            Model uiModel
    ){

        Person person = personService.getPersonById(id);

        if(person == null) throw new ResourceNotFoundException("there is not Person with id="+id);

        uiModel.addAttribute("person", person);

        return (ajax == null)? "persons/edit" : "ajax/persons/edit";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    @ResponseBody() public byte[] editPerson(Model uiModel, Person person){

        Gson gson = new Gson();
        PersonUpdateAjaxAction pua = new PersonUpdateAjaxAction();

        try{
            Person p = personService.edit(person);
            pua.success = true;
            pua.person = p;
            pua.personAjax = new PersonAjax(p.getId(), p.toString());
        }
        finally {
            String res = gson.toJson(pua);
            try {
                return res.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                return res.getBytes(); // нет utf-8;
            }
        }

    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html", value = "/{id}")
    @ResponseBody() public byte[] editPersonPut(
            Model uiModel, Person person,
                @PathVariable(value = "id") Long id
            ){
        person.setId(id);

        return editPerson(uiModel, person);
    }
}
