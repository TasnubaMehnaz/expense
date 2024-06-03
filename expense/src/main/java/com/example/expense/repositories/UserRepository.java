package com.example.expense.repositories;

import com.example.expense.domain.User;
import com.example.expense.exceptions.EtAuthException;

import java.util.Set;

public interface UserRepository {
    Integer create(String firstName, String lastName, String email, String password,String role) throws EtAuthException;

    User findByEmailAndPassword(String email, String password) throws EtAuthException;

    Integer getCountByEmail(String email);

    User findById(Integer userId);
}
