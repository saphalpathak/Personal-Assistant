package com.wicc.personalassistant.utils.validators;

import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.utils.CurrentUserDetailService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class UserValidator {
    private final CurrentUserDetailService currentUserDetailService;

    public UserValidator(CurrentUserDetailService currentUserDetailService) {
        this.currentUserDetailService = currentUserDetailService;
    }

    public boolean isValidTodoId(Integer id) {
        User currentUser = currentUserDetailService.getCurrentUser();
        List<Todo> todos = currentUser.getTodos();
        boolean isValid = false;
        for (Todo todo : todos) {
            if (Objects.equals(todo.getId(), id)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    public boolean isValidTransactionId(Integer id){
        User currentUser = currentUserDetailService.getCurrentUser();
        List<Transaction> transactions = currentUser.getTransactions();
        boolean isValid = false;
        for (Transaction transaction : transactions) {
            if (Objects.equals(transaction.getId(), id)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

}
