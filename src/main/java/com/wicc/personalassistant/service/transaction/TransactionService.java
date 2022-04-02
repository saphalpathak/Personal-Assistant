package com.wicc.personalassistant.service.transaction;

import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.service.GenericCrudService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface TransactionService extends GenericCrudService<TransactionDto,Integer> {

    Map<String,Integer> currentMonthIncomes(Integer userId);

    Map<String,Integer> currentMonthExpenses(Integer userId);

    //getting all the category of particular user
    List<String> getAllCategory(Integer id);
}
