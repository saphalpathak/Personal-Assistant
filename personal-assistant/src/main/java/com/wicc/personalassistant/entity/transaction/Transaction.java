package com.wicc.personalassistant.entity.transaction;
import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @SequenceGenerator(name = "transaction_id_GEN",sequenceName = "transaction_id_GEN",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_GEN")
    private Integer id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date",nullable = false)
    private Date date;

    @Column(name = "amount",nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "FK_transaction_user"))
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",referencedColumnName = "id",foreignKey = @ForeignKey(name = "FK_transaction_category"))
    private Category category;


    //converting dto to entity
    public Transaction(TransactionDto transactionDto){
        this.id = transactionDto.getId();
        this.title = transactionDto.getTitle();
        this.description = transactionDto.getDescription();
        this.amount = transactionDto.getAmount();
        this.category = transactionDto.getCategory();
        this.date = transactionDto.getTransactionDate();
        this.user = transactionDto.getUser();
        this.category = transactionDto.getCategory();
    }



}
