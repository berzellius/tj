package com.tajinsurance.web;

import com.tajinsurance.domain.*;
import com.tajinsurance.service.CatContractService;
import com.tajinsurance.service.CatContractStatusService;
import com.tajinsurance.service.PartnerService;
import com.tajinsurance.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    @Autowired
    CatContractService catContractService;

    @Autowired
    CatContractStatusService catContractStatusService;

    @Autowired
    PersonService personService;

    @Autowired
    PartnerService partnerService;

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
        registry.addConverter(getIdToCatContractConverter());
        registry.addConverter(getStringToCatContractConverter());
        registry.addConverter(getIdToCatContractStatusConverter());
        registry.addConverter(getStringToCatContractStatusConverter());
        registry.addConverter(getIdToPartnerCoverter());
        registry.addConverter(getStringToPartnerConverter());
        registry.addConverter(getIdToPersonConverter());
        registry.addConverter(getStringToPersonConverter());
        registry.addConverter(getStringToDateConverter());
        registry.addConverter(getDateToStringConverter());
		// Register application converters and formatters
	}

    public Converter<Long, CatContract> getIdToCatContractConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.tajinsurance.domain.CatContract>() {
            @Override
            public com.tajinsurance.domain.CatContract convert(Long id) {
                return catContractService.getCatContractById(id);
            }
        };
    }

    public Converter<String, CatContract> getStringToCatContractConverter(){
        return new Converter<String, CatContract>() {
            @Override
            public CatContract convert(String s) {
                Long id = Long.decode(s);
                return catContractService.getCatContractById(id);
            }
        };
    }

    public Converter<Long, CatContractStatus> getIdToCatContractStatusConverter(){
        return new Converter<Long, CatContractStatus>() {
            @Override
            public CatContractStatus convert(Long id){
                return catContractStatusService.getCatContractStatusById(id);
            }
        };
    }

    public Converter<String, CatContractStatus> getStringToCatContractStatusConverter(){
        return new Converter<String, CatContractStatus>() {
            @Override
            public CatContractStatus convert(String s) {
                Long id = Long.decode(s);
                return catContractStatusService.getCatContractStatusById(id);
            }
        };
    }

    public Converter<Long, Partner> getIdToPartnerCoverter(){
        return new Converter<Long, Partner>() {
            @Override
            public Partner convert(Long id) {
                return partnerService.getPartnerById(id);
            }
        };
    }

    public Converter<String, Partner> getStringToPartnerConverter(){
        return new Converter<String, Partner>() {
            @Override
            public Partner convert(String s) {
                Long id = Long.decode(s);
                return partnerService.getPartnerById(id);
            }
        };
    }

    public Converter<Long, Person> getIdToPersonConverter(){
        return new Converter<Long, Person>() {
            @Override
            public Person convert(Long id) {
                return personService.getPersonById(id);
            }
        };
    }

    public Converter<String, Person> getStringToPersonConverter(){
        return new Converter<String, Person>() {
            @Override
            public Person convert(String s) {
                Long id = Long.decode(s);
                return personService.getPersonById(id);
            }
        };
    }

    public Converter<String, Date> getStringToDateConverter(){
        return new Converter<String, Date>() {
            @Override
            public Date convert(String s) {
                try {
                    Date dt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(s);
                    return dt;
                } catch (ParseException e) {
                    try {
                        Date dt = new SimpleDateFormat("dd.MM.yyyy").parse(s);
                        return dt;
                    } catch (ParseException e1) {
                        return null;
                    }
                }
            }
        };
    }

    public Converter<Date,String> getDateToStringConverter(){
        return new Converter<Date, String>() {
            @Override
            public String convert(Date date) {
                return  new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
            }
        };
    }

}
