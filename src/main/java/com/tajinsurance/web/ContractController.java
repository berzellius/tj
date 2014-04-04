package com.tajinsurance.web;

import com.google.gson.Gson;
import com.tajinsurance.domain.*;
import com.tajinsurance.dto.ContractPremiumAjax;
import com.tajinsurance.dto.ContractPrintAjax;
import com.tajinsurance.dto.RiskAjax;
import com.tajinsurance.exceptions.EntityNotSavedException;
import com.tajinsurance.exceptions.NoEntityException;
import com.tajinsurance.service.*;
import com.tajinsurance.utils.UserLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;


@RequestMapping("/contracts")
@Controller
@RooWebScaffold(path = "contracts", formBackingObject = Contract.class)
public class ContractController {
    @Autowired
    ContractService contractService;

    @Autowired
    CatContractService catContractService;

    @Autowired
    CatContractStatusService catContractStatusService;

    @Autowired
    PartnerService partnerService;

    @Autowired
    PersonService personService;

    @Autowired
    UserLoginUtil userLoginUtil;

    @Autowired
    PremiumService premiumService;

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(Contract contract, Model uiModel, HttpServletRequest httpServletRequest) {
        try {
            contractService.save(contract);
            uiModel.asMap().clear();
            return "redirect:/contracts/" + encodeUrlPathSegment(contract.getId().toString(), httpServletRequest);
        } catch (EntityNotSavedException e) {
            uiModel.addAttribute("error", e.getMessage());
            return "redirect:/contracts/";
        }
    }


    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(
            Model uiModel, HttpServletRequest httpServletRequest,
            @RequestParam(value = "category", required = false)
            Long categoryId
    ) {

        User currectUser = userLoginUtil.getCurrentLogInUser();
        Locale locale = LocaleContextHolder.getLocale();

        CatContract cc = catContractService.getCatContractById(categoryId);
        if(cc == null) return "redirect:/contracts?select_category";
                //throw new IllegalArgumentException("there is no contract categories with id = "+categoryId);

        Contract preparedContract = contractService.preparedContract(currectUser, cc);

        //populateEditForm(uiModel, preparedContract);

        uiModel.addAttribute("catContracts", catContractService.getAllowedCatContractsForUser(currectUser, locale.toString()));
        uiModel.addAttribute("catContractStatuses", catContractStatusService.getAllCatContractStatuses(locale.toString()));
        uiModel.addAttribute("partners", partnerService.getPartners());

        return "redirect:/contracts/" + encodeUrlPathSegment(preparedContract.getId().toString(), httpServletRequest) + "?form";
        //return "contracts/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(
            @PathVariable("id") Long id,
            @RequestParam(value = "print", required = false) String print,
            Model uiModel
    ) {

        try {
            if(print != null) uiModel.addAttribute("print_version",true);

            Locale locale = LocaleContextHolder.getLocale();
            Contract contract = contractService.getContractById(id);
            uiModel.addAttribute("contract", contract);
            uiModel.addAttribute("cur", contract.getCatContract().getCurrency());
            uiModel.addAttribute("itemId", id);

            List<ContractPremiumAjax> validatedPremiums = premiumService.getValidatedPremiums(contract);
            uiModel.addAttribute("validatedPremiums", validatedPremiums);

            Person p = contract.getPerson();
            if(p != null) uiModel.addAttribute("person",p);


            /*System.out.println(locale.toString());*/
        } catch (NoEntityException e) {
            uiModel.addAttribute("error", e.getMessage());
        }

        if(print != null) return "contracts/print";

        return "contracts/show";
    }


    @RequestMapping(produces = "text/html")
    public String list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sortFieldName", required = false) String sortFieldName,
            @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {

        List<Contract> contracts = contractService.getContractsPage(page, size, sortFieldName, sortOrder);

        uiModel.addAttribute("contracts", contracts);

        uiModel.addAttribute("maxPages", contractService.countPages(size));

        return "contracts/clist";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(Contract contract, Model uiModel, HttpServletRequest httpServletRequest) {
        try {
            // Обрабатываем форму договора в зависимости от статуса.
            switch (contract.getCatContractStatus().getCode()) {
                case BEGIN:
                    if (contract.getPerson() == null) {
                        contractService.update(contract);
                        return "redirect:/contracts/" + encodeUrlPathSegment(contract.getId().toString(), httpServletRequest) + "?form&reason=person_not_filled";
                    }

                    if (
                            (premiumService.getNotValidatedPremiums(contract) == null)
                         && (premiumService.getValidatedPremiums(contract) == null)
                        )
                    {
                        contractService.update(contract);
                        return "redirect:/contracts/" + encodeUrlPathSegment(contract.getId().toString(), httpServletRequest) + "?form&reason=risk_not_filled";
                    }

                    contract.setCatContractStatus(catContractStatusService.getCatContractStatusByCode(CatContractStatus.StatusCode.NEW));

                    break;

            }
            contractService.update(contract);
            uiModel.asMap().clear();

        } catch (NoEntityException e) {
            //e.printStackTrace();
            uiModel.addAttribute("error", e.getMessage());
        }
        return "redirect:/contracts/" + encodeUrlPathSegment(contract.getId().toString(), httpServletRequest) + "?form";
    }


    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id,
                             Model uiModel,
                             @RequestParam(value = "reason", required = false) String reason) {
        try {
            if (reason != null) {
                uiModel.addAttribute("errorMsg", reason);
            }

            User currectUser = userLoginUtil.getCurrentLogInUser();
            Locale locale = LocaleContextHolder.getLocale();
            Contract c = contractService.getContractById(id);
            populateEditForm(uiModel, c);

            switch (c.getCatContractStatus().getCode()){
                case BEGIN:
                    uiModel.addAttribute("printable", false);
                break;
                default:
                    uiModel.addAttribute("printable", true);
                break;
            }

            List<RiskAjax> risks = catContractService.getAllowedRisksForCatContract(c.getCatContract());

            List<ContractPremiumAjax> notValidatedPremiums = premiumService.getNotValidatedPremiums(c);

            List<ContractPremiumAjax> validatedPremiums = premiumService.getValidatedPremiums(c);

            uiModel.addAttribute("validatedPremiums", validatedPremiums);
            uiModel.addAttribute("notValidatedPremiums", notValidatedPremiums);


            uiModel.addAttribute("allowedRisks", risks);

            uiModel.addAttribute("cur", (
                    c.getCatContract().getCurrency() == null ? "N/A" : c.getCatContract().getCurrency().getVal())
            );

            uiModel.addAttribute("catContracts", catContractService.getAllowedCatContractsForUser(currectUser, locale.toString()));
            uiModel.addAttribute("catContractStatuses", catContractStatusService.getAllCatContractStatuses(locale.toString()));
            uiModel.addAttribute("partners", partnerService.getPartners());

        } catch (NoEntityException e) {
            uiModel.addAttribute("error", e.getMessage());
        }
        return "contracts/create";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        try {
            contractService.delete(id);
            uiModel.asMap().clear();
            uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
            uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        } catch (NoEntityException e) {
            uiModel.addAttribute("error", e.getMessage());
        }
        return "redirect:/contracts";
    }

    void populateEditForm(Model uiModel, Contract contract) {
        uiModel.addAttribute("contract", contract);
        uiModel.addAttribute("person_id", (contract.getPerson() == null ? "0" : contract.getPerson().getId().toString()));
        uiModel.addAttribute("person", (contract.getPerson() == null ? "" : contract.getPerson().toString()));
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }

    @RequestMapping(params = "select_category")
    String selectCategory(Model uiModel){
        User currectUser = userLoginUtil.getCurrentLogInUser();
        Locale locale = LocaleContextHolder.getLocale();

        uiModel.addAttribute("contractCategories", catContractService.getAllowedCatContractsForUser(currectUser, locale.toString() ));

        return "contracts/select_category";
    }

    @RequestMapping(params = "print", method = RequestMethod.GET)
    @ResponseBody byte[] printContract(
            @RequestParam(value = "id") Long id
    ){

        Gson gson = new Gson();
        ContractPrintAjax cpa = new ContractPrintAjax();

        try {
            Contract c = contractService.getContractById(id);
            cpa.printDate = contractService.printContract(c);
            cpa.success = true;
        } catch (NoEntityException e) {
            cpa.success = false;
            cpa.message = "Cant find Contract entity #"+id;
        }
        finally {
            String g = gson.toJson(cpa);
            try {
                return g.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
