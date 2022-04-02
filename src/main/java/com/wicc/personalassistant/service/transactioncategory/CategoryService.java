package com.wicc.personalassistant.service.transactioncategory;

import com.wicc.personalassistant.dto.ResponseDto;
import com.wicc.personalassistant.dto.transactioncategory.CategoryDto;
import com.wicc.personalassistant.service.GenericCrudService;

import java.util.List;

public interface CategoryService{
    ResponseDto save(CategoryDto categoryDto);
    List<CategoryDto> findAll();
    CategoryDto findById(Integer id);
    void deleteById(Integer id);
}
