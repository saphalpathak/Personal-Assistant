package com.wicc.personalassistant.dto.transactioncategory;

import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto{

    private Integer id;

    @NotEmpty(message = "Name can not be empty!!")
    private String name;

    @NotEmpty(message = "Description can not be empty!!")
    private String description;

    @Column(name = "transactionStatus",nullable = false)
    private TransactionStatus transactionStatus;

    private List<Transaction> transactions;

    //converting entity to dto
    public CategoryDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.transactions = category.getTransactions();
        this.transactionStatus = category.getTransactionStatus();
    }

    //converting list of entities to dtos
    public static List<CategoryDto> toDtos(List<Category> categories){
        List<CategoryDto> CategoryDtos = new ArrayList<>();
        categories.forEach(category-> CategoryDtos.add(new CategoryDto(category)));
        return CategoryDtos;
    }
}
