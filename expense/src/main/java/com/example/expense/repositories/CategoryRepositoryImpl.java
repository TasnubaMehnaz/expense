package com.example.expense.repositories;
import com.example.expense.domain.Category;
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
public class CategoryRepositoryImpl implements CategoryRepository{

    private static final String SQL_CREATE = "INSERT INTO ET_CATEGORIES (USER_ID, TITLE, DESCRIPTION) VALUES( ?, ?, ?)";
//    private static final String SQL_FIND_BY_ID = "SELECT C.CATEGORY_ID " +
//                "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
//                "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
//                "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID";
    private static final String SQL_FIND_ALL = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT), 0) AS TOTAL_EXPENSE FROM ET_CATEGORIES C LEFT OUTER JOIN ET_TRANSACTIONS T ON C.CATEGORY_ID = T.CATEGORY_ID WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID,C.USER_ID, C.TITLE, C.DESCRIPTION";
    private static final String SQL_FIND_BY_ID = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT), 0) AS TOTAL_EXPENSE FROM ET_CATEGORIES C LEFT OUTER JOIN ET_TRANSACTIONS T ON C.CATEGORY_ID = T.CATEGORY_ID WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID,C.USER_ID, C.TITLE, C.DESCRIPTION";
    // private static final String SQL_FIND_BY_ID ="SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION," + "COALESCE(SUM(T.AMOUNT),0)TOTAL EXPENSE" + "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID" + "WHERE C.USER_ID=? AND C.CATEGORY_ID=? GROUP BY C.CATEGORY_ID";
    private static final String SQL_UPDATE = "UPDATE ET_CATEGORIES SET TITLE=?, DESCRIPTION=? WHERE USER_ID=? AND CATEGORY_ID=?";
    private static final String SQL_DELETE = "DELETE FROM ET_CATEGORIES WHERE USER_ID=? AND CATEGORY_ID=?";
    private static final String SQL_DELETE_TRANSACTION = "DELETE FROM ET_TRANSACTIONS WHERE CATEGORY_ID=?";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> findAll(Integer userId) throws EtResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, new Object[]{userId},categoryRowmapper);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws EtBadRequestException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{userId, categoryId},categoryRowmapper);
        } catch (Exception e){
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws EtBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"CATEGORY_ID"});
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);
//            Object categoryIdValue = keyHolder.getKeys().get("CATEGORY_ID");
//            Class<?> categoryIdType = categoryIdValue.getClass();
//            System.out.println("Type of CATEGORY_ID: " + categoryIdType.getName());
            //System.out.println(keyHolder.getKeys().get("CATEGORY_ID"));
            BigDecimal categoryIdValue = (BigDecimal) keyHolder.getKeys().get("CATEGORY_ID");
            Integer CATEGORY_ID_VAL = categoryIdValue.intValue();
            return CATEGORY_ID_VAL;
        } catch (Exception e){
            throw new EtBadRequestException("Invalid Request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{category.getTitle(), category.getDescription(),userId, categoryId});
        }catch (Exception e){
            throw new EtBadRequestException("Invalid update request!");
        }


    }

    @Override
    public void removeById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        this.removeAllTransaction(categoryId);
        int count= jdbcTemplate.update(SQL_DELETE, new Object[]{userId, categoryId});
        if(count==0){
            throw new EtResourceNotFoundException("Category not found to remove!");
        }
    }
    public void removeAllTransaction(Integer categoryId) throws EtResourceNotFoundException{
        int count = jdbcTemplate.update(SQL_DELETE_TRANSACTION, new Object[]{categoryId});
        if(count==0){
            throw new EtResourceNotFoundException("No transactions found for the category!");
        }
    }

    private RowMapper<Category> categoryRowmapper = ((rs, rowNum) ->{
        return new Category(rs.getInt("CATEGORY_ID"),
                rs.getInt("USER_ID"),
                rs.getString("TITLE"),
                rs.getString("DESCRIPTION"),
                rs.getDouble("TOTAL_EXPENSE"));
    } );
}
