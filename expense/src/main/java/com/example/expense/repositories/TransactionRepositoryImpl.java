package com.example.expense.repositories;
import com.example.expense.domain.Category;
import com.example.expense.domain.Transaction;
import com.example.expense.exceptions.EtBadRequestException;
import com.example.expense.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
@Repository
public class TransactionRepositoryImpl implements TransactionRepository{

    private static final String SQL_FIND_BY_ID = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE  USER_ID=? AND CATEGORY_ID=? AND TRANSACTION_ID=?";
    private static final String SQL_CREATE = "INSERT INTO ET_TRANSACTIONS ( CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE) VALUES (?,?,?,?,?)";
    private static final String SQL_FIND_ALL="SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID=? AND CATEGORY_ID=?";
    private static final String SQL_UPDATE="UPDATE ET_TRANSACTIONS SET AMOUNT=?, NOTE=?, TRANSACTION_DATE=? WHERE  USER_ID=? AND CATEGORY_ID=? AND TRANSACTION_ID=? ";
    private static final String SQL_DELETE="DELETE FROM ET_TRANSACTIONS WHERE USER_ID=? AND CATEGORY_ID=? AND TRANSACTION_ID=?";
    private static final String SQL_FIND_AMOUNT_BY_ID= "SELECT AMOUNT FROM ET_TRANSACTIONS WHERE TRANSACTION_ID=?";
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public List<Transaction> findAll(Integer userId, Integer categoryId) {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId,categoryId},transactionRowMapper);
    }

    @Override
    public Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, categoryId, transactionId},transactionRowMapper);
        } catch (Exception e){
            throw new EtResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public String findAmountById(Integer transactionId) throws EtResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_AMOUNT_BY_ID, new Object[]{transactionId}, String.class);
        } catch (Exception e){
            throw new EtResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection ->{
                    PreparedStatement ps= connection.prepareStatement(SQL_CREATE, new String[]{"TRANSACTION_ID"} ); //,PreparedStatement.RETURN_GENERATED_KEYS
                    ps.setInt(1, categoryId);
                    ps.setInt(2,userId);
                    ps.setDouble(3, amount);
                    ps.setString(4, note);
                    ps.setLong(5, transactionDate);
                return ps;
            }, keyHolder);
            BigDecimal transactionIdIdValue = (BigDecimal) keyHolder.getKeys().get("TRANSACTION_ID");
            Integer TRANSACTION_ID_VAL = transactionIdIdValue.intValue();
            return TRANSACTION_ID_VAL;

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new EtBadRequestException("Invalid transaction create request!");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{transaction.getAmount(), transaction.getNote(), transaction.getTransactionDate(), userId, categoryId, transactionId});
        } catch (Exception e){
            throw new EtBadRequestException("Failed to update");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException {
        int count=jdbcTemplate.update(SQL_DELETE,new Object[]{ userId, categoryId, transactionId});
        if(count==0){
            throw new EtResourceNotFoundException("Failed to delete!");
        }

    }

    private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) ->{
        return new Transaction(rs.getInt("TRANSACTION_ID"),
                rs.getInt("CATEGORY_ID"),
                rs.getInt("USER_ID"),
                rs.getDouble("AMOUNT"),
                rs.getString("NOTE"),
                rs.getLong("TRANSACTION_DATE"));
    } );
}
