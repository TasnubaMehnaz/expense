package com.example.expense.repositories;

import com.example.expense.domain.User;
import com.example.expense.exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;

//import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Repository
public class UserRepositoryImpl implements UserRepository{


    private static final String SQL_CREATE = "INSERT INTO ET_USERS (FIRSTNAME, LASTNAME, EMAIL, PASSWORD) VALUES( ?, ?, ?, ?)";

    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";

    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRSTNAME, LASTNAME,EMAIL,PASSWORD FROM ET_USERS WHERE USER_ID=?";

    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD FROM ET_USERS WHERE EMAIL=?";
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                System.out.println("Executing SQL statement...");
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"USER_ID"});
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, password);
                return ps;
            }, keyHolder);
            System.out.println("SQL statement executed successfully.");
            Number key = keyHolder.getKey() != null ? Long.parseLong(keyHolder.getKey().toString()) : null;
            if (key != null) {
                return key.intValue();
            } else {
                throw new EtAuthException("Failed to retrieve generated key.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new EtAuthException("Invalid details, Failed to create account.");
        }
    }



    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, new Object[]{email});
            if (!password.equals(user.getPassword()))
                throw new EtAuthException("Invalid password");
            return user;
        } catch (EmptyResultDataAccessException e){
            throw new EtAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL,int.class, email);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,userRowMapper, new Object[]{userId});
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum)  -> {
        return new User(rs.getInt("USER_ID"),
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"));
    });
}
