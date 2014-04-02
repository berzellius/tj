package com.tajinsurance.web;

import com.google.gson.Gson;
import com.tajinsurance.domain.Contract;
import com.tajinsurance.domain.ContractPremium;
import com.tajinsurance.domain.Risk;
import com.tajinsurance.dto.CalculateRiskAjaxAction;
import com.tajinsurance.dto.ContractPremiumAjax;
import com.tajinsurance.dto.DeletePremuimAjaxAction;
import com.tajinsurance.exceptions.NoEntityException;
import com.tajinsurance.service.CatContractService;
import com.tajinsurance.service.ContractService;
import com.tajinsurance.service.PremiumService;
import com.tajinsurance.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by berz on 30.03.14.
 */
@RequestMapping("/risk")
@Controller
public class RiskController {
    @Autowired
    ContractService contractService;

    @Autowired
    CatContractService catContractService;

    @Autowired
    RiskService riskService;

    @Autowired
    PremiumService premiumService;

    @RequestMapping(params = "calc", method = RequestMethod.POST)
    @ResponseBody
    byte[] calcRisk(
        @RequestParam(value = "contract") Long contractId,
        @RequestParam(value = "risk") Long riskId,
        @RequestParam(value = "sum") BigDecimal sum
    ){
        Gson gson = new Gson();
        CalculateRiskAjaxAction calc = new CalculateRiskAjaxAction();

        try {
            Risk risk = riskService.getRiskById(riskId);
            Contract contract = contractService.getContractById(contractId);

            if(!contractService.checkIfTheRiskIsAllowed(risk, contract)){
                calc.success = false;
                calc.message = "Risk not allowed!";
            }

            ContractPremium contractPremium = premiumService.calculatePremium(new ContractPremium(risk, contract, sum));

            calc.contractPremiumAjax = new ContractPremiumAjax(risk.getValue(), sum, contractPremium.getPremium(), contractPremium.getId());

            //calc.contractPremium = contractPremium;
            calc.success = true;

        } catch (NoEntityException e) {
            calc.success = false;
            calc.message = "object not found!";
        }
        finally {
            try {
                return gson.toJson(calc).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                return gson.toJson(calc).getBytes();
            }
        }



    }

    @RequestMapping(params = "del")
    @ResponseBody byte[] deletePremium(
            @RequestParam(value = "id") Long id
    ){
        Gson gson = new Gson();
        DeletePremuimAjaxAction del = new DeletePremuimAjaxAction();

        try {
            del.success = premiumService.deletePremium(id);
            del.id = id;
        } catch (NoEntityException e) {
            del.success = false;
            del.message = "premium not found";
        }
        finally {
            try {
                return gson.toJson(del).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                return gson.toJson(del).getBytes();
            }
        }
    }

}
