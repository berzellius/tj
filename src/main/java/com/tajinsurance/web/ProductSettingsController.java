package com.tajinsurance.web;

import com.tajinsurance.domain.*;
import com.tajinsurance.exceptions.EntityNotSavedException;
import com.tajinsurance.exceptions.NoEntityException;
import com.tajinsurance.service.CatContractService;
import com.tajinsurance.service.PartnerService;
import com.tajinsurance.service.RiskService;
import com.tajinsurance.utils.LanguageUtil;
import com.tajinsurance.utils.UserLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
/**
 * Created by berz on 01.11.14.
 */
@RequestMapping("/productsettings")
@Controller
public class ProductSettingsController {

    @Autowired
    CatContractService catContractService;

    @Autowired
    UserLoginUtil userLoginUtil;

    @Autowired
    RiskService riskService;

    @Autowired
    PartnerService partnerService;

    @Autowired
    LanguageUtil languageUtil;

    @RequestMapping(method = RequestMethod.GET)
    public String chooseProduct(
            Model uiModel
    ){
        Locale locale = LocaleContextHolder.getLocale();
        List<CatContract> catContractList = catContractService.getCatContractsGlobalSettings(locale.getLanguage());
        uiModel.addAttribute("contractCategories", catContractList);

        return "product_settings/choose";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit/{id}")
    String editProduct(
            Model uiModel,
            @PathVariable("id") Long catContractId,
            @RequestParam(value = "reason", required = false)
            String reason,
            @RequestParam(value = "property_reason", required = false)
            String property_reason
    ){

        Locale locale = LocaleContextHolder.getLocale();

        User user = userLoginUtil.getCurrentLogInUser();

        if(reason != null){
            uiModel.addAttribute("reason", reason);
        }

        if(property_reason != null){
            uiModel.addAttribute("property_reason", property_reason);
        }

        CatContract catContract = (CatContract) languageUtil.getLocalizatedObject(catContractService.getCatContractById(catContractId), locale.getLanguage());

        // Уже рассчитанные риски
        List<CatContractRisk> calculatedRisk = riskService.getExistRisks(null, catContract);
        // Риски, которые еще можно рассчитать
        List<Risk> potentialRisks = riskService.getPotentialRiskTermRisks(null, catContract);


        uiModel.addAttribute("calculatedRisks", calculatedRisk);
        uiModel.addAttribute("potentialRisks", potentialRisks);

        uiModel.addAttribute("catContract", catContract);


        // Денежные параметры, которые уже заданы для партнера (по сериям) и те, которые могут быть заданы
        List<ProductMoneyProperty> existsProperties = partnerService.getProductMoneyProperties(catContract);
        List<ProductMoneyProperty> potentialProperties = partnerService.getProductMoneyPropertiesNotSet(catContract);

        uiModel.addAttribute("existsProp", existsProperties);
        uiModel.addAttribute("potentialProp", potentialProperties);


        // редактора франшиз не будет
        /*if(catContract.getProduct().equals(CatContract.Product.MP0)){
            PartnerProductMoneyProperty franchiseProperty = partnerService.getPartnerProductMoneyPropertyByPropertyName(partner, ProductMoneyProperty.PropertyType.MP0_franchise_k, catContract);

            if(franchiseProperty != null && franchiseProperty.getUseProperty() != null && franchiseProperty.getUseProperty()){
                uiModel.addAttribute("useFranchise", true);
            }
        }*/

        return "product_settings/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}", params = "new_property")
    public String newProperty(
            @PathVariable(value = "id")
            Long catContractId,
            @RequestParam(value = "property_id")
            Long propertyId,
            @RequestParam(value = "value")
            BigDecimal value,
            @RequestParam(value = "use_property", required = false)
            Boolean useProperty,
            Model uiModel
    ){

        CatContract catContract = catContractService.getCatContractById(catContractId);
        ProductMoneyProperty productMoneyProperty = partnerService.getProductMoneyPropertyById(propertyId);
        productMoneyProperty.setMoneyValue(value);
        productMoneyProperty.setUseProperty(useProperty);
        try {
            partnerService.updateParametersForProductMoneyProperty(productMoneyProperty);
        } catch (EntityNotSavedException e) {
            uiModel.addAttribute("property_reason", e.getMessage());
            return "redirect:/productsettings/edit/"+catContractId;
        }

        return "redirect:/productsettings/edit/"+catContractId;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/edit/{id}", params = "delete_property")
    public String deleteProperty(
            @PathVariable(value = "id")
            Long catContractId,
            @RequestParam(value = "pr_id")
            Long propertyId,
            Model uiModel
    ){

        partnerService.clearProductPropertyGlobalParams(propertyId);

        return "redirect:/productsettings/edit/"+catContractId;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit/{id}", params = "new_risk")
    public String newRisk(
            @PathVariable(value = "id")
            Long catContractId,
            @RequestParam(value = "risk")
            Long riskId,
            @RequestParam(value = "rate")
            BigDecimal rate,
            @RequestParam(value = "monthTarif")
            BigDecimal monthTarif,
            @RequestParam(value = "minSum")
            BigDecimal minSum,
            @RequestParam(value = "maxSum")
            BigDecimal maxSum
    ){
        Risk risk = null;
        try {
            risk = riskService.getRiskById(riskId);

            CatContract catContract = catContractService.getCatContractById(catContractId);

            CatContractRisk catContractRisk = new CatContractRisk();
            catContractRisk.setCatContract(catContract);
            catContractRisk.setPartner(null);
            catContractRisk.setRisk(risk);
            catContractRisk.setMonthTarif(monthTarif);
            catContractRisk.setRate(rate);
            catContractRisk.setMinSum(minSum);
            catContractRisk.setMaxSum(maxSum);

            riskService.newCatContractRisk(catContractRisk);
        } catch (NoEntityException e) {
            //
        }



        return "redirect:/productsettings/edit/"+catContractId;
    }


    @RequestMapping(method = RequestMethod.DELETE,value = "/edit/{id}", params = "delete_risk")
    public String deleteRisk(
            @PathVariable(value = "id")
            Long catContractId,
            @RequestParam(value = "id")
            Long catContractRiskId
    ){
        CatContractRisk catContractRisk = riskService.getCatContractRiskById(catContractRiskId);
        riskService.removeCatContractRisk(catContractRisk);

        return "redirect:/productsettings/edit/"+catContractId;
    }

}
