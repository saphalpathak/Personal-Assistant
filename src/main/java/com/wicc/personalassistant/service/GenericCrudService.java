package com.wicc.personalassistant.service;

import com.wicc.personalassistant.dto.ResponseDto;

import java.util.List;

public interface GenericCrudService<D,ID> {
    ResponseDto save(D d);
    List<D> findAll(ID d);
    D findById(ID id);
    void deleteById(ID id);
}
