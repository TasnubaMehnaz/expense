package com.example.expense.services;

import com.example.expense.domain.User;
import com.example.expense.exceptions.EtAuthException;
import com.example.expense.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserServicesImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if (email!=null) email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password, String role) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email !=null) email =email.toLowerCase();
        if (!pattern.matcher(email).matches())
            throw new EtAuthException("Invalid Email Format");
        Integer count = userRepository.getCountByEmail(email);
        if (count>0)
            throw new EtAuthException("Email already in use");
        //Set<String> roles= Set.of("USER");//for roles
        Integer userId = userRepository.create(firstName, lastName, email, password, role);
        System.out.println(userId);
        return userRepository.findById(userId);
    }
}
