package com.example.expense.repositories;

import com.example.expense.domain.Category;
import com.example.expense.exceptions.EtBadRequestException;
import com.example.expense.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll(Integer userId) throws EtResourceNotFoundException;
    Category findById(Integer userId, Integer categoryId) throws EtBadRequestException;
    Integer create(Integer userId, String title, String description) throws EtBadRequestException;
    void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;
    void removeById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;
}
