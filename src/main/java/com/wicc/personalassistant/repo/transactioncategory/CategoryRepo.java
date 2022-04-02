package com.wicc.personalassistant.repo.transactioncategory;

import com.wicc.personalassistant.entity.transactioncategory.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
