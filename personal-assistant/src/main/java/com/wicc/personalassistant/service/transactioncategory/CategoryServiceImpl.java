package com.wicc.personalassistant.service.transactioncategory;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transactioncategory.CategoryDto;
import com.wicc.personalassistant.entity.transactioncategory.Category;
import com.wicc.personalassistant.enums.TransactionStatus;
import com.wicc.personalassistant.repo.transactioncategory.CategoryRepo;
import com.wicc.personalassistant.service.transaction.TransactionService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepo categoryRepo;


    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    //create new category

    @Override
    public ResponseDto save(CategoryDto categoryDto) {
        try {
            Category category = new Category(categoryDto);
           categoryRepo.save(category);

            //returning value as a response dto
            return new ResponseDto("Category added!!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto("Category addition failed!!");
        }
    }

    //finding all the tasks
    @Override
    public List<CategoryDto> findAll() {
        return CategoryDto.toDtos(categoryRepo.findAll());
    }

    //finding Category by id
    @Override
    public CategoryDto findById(Integer integer) {
        Category category;
        try {
            category = categoryRepo.findById(integer).orElseThrow(
                    () -> new RuntimeException("Not Found")
            );
            return new CategoryDto(category);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(Integer integer) {
        categoryRepo.deleteById(integer);
    }

    @Override
    public List<CategoryDto> getAllIncomeCategories() {
        return findAll().stream()
                .filter(category-> category.getTransactionStatus()
                        .equals(TransactionStatus.INCOME))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getAllExpensesCategories() {
        return findAll().stream()
                .filter(category-> category.getTransactionStatus()
                        .equals(TransactionStatus.EXPENSES))
                .collect(Collectors.toList());
    }
}
