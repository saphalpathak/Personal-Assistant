package com.wicc.personalassistant.entity.transactioncategory;

import com.wicc.personalassistant.dto.transactioncategory.CategoryDto;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @SequenceGenerator(name = "category_id_GEN",sequenceName = "category_id_GEN",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_GEN")
    private Integer id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "transactionStatus",nullable = false)
    private TransactionStatus transactionStatus;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    List<Transaction> transactions;


    //converting dto to entity
    public Category(CategoryDto categoryDto){
        this.id = categoryDto.getId();
        this.name = categoryDto.getName();
        this.description = categoryDto.getDescription();
        this.transactionStatus = categoryDto.getTransactionStatus();
    }
}
