package com.tajinsurance.service;

import com.tajinsurance.domain.CatContract;
import com.tajinsurance.domain.Contract;
import com.tajinsurance.domain.Risk;
import com.tajinsurance.domain.User;
import com.tajinsurance.exceptions.EntityNotSavedException;
import com.tajinsurance.exceptions.NoEntityException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by berz on 27.02.14.
 */
@Service
public interface ContractService {
    /**
     * Контракт по id
     * @param id
     * @return
     */
    Contract getContractById(Long id) throws NoEntityException;

    Contract getContractById(Long id, String locale) throws NoEntityException;

    /**
     * Получить список контрактов
     * @param first - начиная с какого контракта
     * @param count - количество
     * @param sortFieldName - поле для сортировки
     * @param sortOrder - порядок сортировки
     * @return
     */
    List<Contract> getContracts(int first, int count, String sortFieldName, String sortOrder);

    /**
     * Получить список контрактов
     * @param first - начиная с какого контракта
     * @param count - количество
     * @return
     */
    List<Contract> getContracts(int first, int count);

    /**
     *
     * @param numPage
     * @param itemsToPage
     * @param sortFieldName
     * @param sortOrder
     * @return
     */
    List<Contract> getContractsPage(Integer numPage, Integer itemsToPage, String sortFieldName, String sortOrder);

    /**
     * Количество контрактов
     * @return
     */
    Long countContracts();


    /**
     * Сущность, подготовленная для первоначального заполнения
     * @return
     */
    Contract preparedContract(User u, CatContract cc);

    /**
     * Проверить, допустим ли расчет риска для договора
     * @param risk - риск
     * @param contract - договор
     * @return
     */
    boolean checkIfTheRiskIsAllowed(Risk risk, Contract contract);


    /**
     * Бизнес логика: печать договора
     * @param c
     */
    Date printContract(Contract c);

    /**
     * Количество страниц
     * @param size - контрактов на страницу
     * @return
     */
    Integer countPages(Integer size);

    void save(Contract contract) throws EntityNotSavedException;

    void update(Contract contract) throws NoEntityException;

    void delete(Long id) throws NoEntityException;
}
