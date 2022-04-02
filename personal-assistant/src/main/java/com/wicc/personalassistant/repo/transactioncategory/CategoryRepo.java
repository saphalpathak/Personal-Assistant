package com.wicc.personalassistant.repo.transactioncategory;

import com.wicc.personalassistant.dto.transaction.TransactionDto;
import com.wicc.personalassistant.entity.todo.Todo;
import com.wicc.personalassistant.entity.transaction.Transaction;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
