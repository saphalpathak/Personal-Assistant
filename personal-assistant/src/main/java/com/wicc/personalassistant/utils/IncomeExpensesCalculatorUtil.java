package com.wicc.personalassistant.utils;

import com.wicc.personalassistant.dto.transaction.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class IncomeExpensesCalculatorUtil {

    public Map<String,Integer> calculateIncomeExpenses(List<TransactionDto> dtos){
        Map<String, Integer> result = new LinkedHashMap<>();
        for(TransactionDto transactionDto : dtos){
            String name = transactionDto.getCategory().getName();
            Integer presentAmount = transactionDto.getAmount();
            boolean b = result.containsKey(name);
            //checking if this category is present or not if present, add the previous and present amount to find total
            //income and expenses on particular category
            if(!b){
                result.put(name,presentAmount);
                continue;
            }
            Integer previousAmount = result.get(name);
            previousAmount += presentAmount;
            result.put(name,previousAmount);
        }
        return result;
    }
}
