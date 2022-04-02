package com.wicc.personalassistant.dto.transaction;

import com.wicc.personalassistant.dto.todo.TodoDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.entity.user.User;
import com.wicc.personalassistant.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Integer id;

    @NotEmpty(message = "Title can not be empty!!")
    private String title;

    private String description;

    private Date transactionDate;

    @NotNull(message = "Amount can not be empty!!")
    private Integer amount;

    private Category category;

    private User user;

    //converting entity to dto
    public TransactionDto(Transaction transaction){
        this.id = transaction.getId();
        this.title = transaction.getTitle();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.category = transaction.getCategory();
        this.transactionDate = transaction.getDate();
        this.user = transaction.getUser();

    }

    //converting list of entities to dtos
    public static List<TransactionDto> toDtos(List<Transaction> transactions){
        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactions.forEach(transaction-> transactionDtos.add(new TransactionDto(transaction)));
        return transactionDtos;
    }





}
