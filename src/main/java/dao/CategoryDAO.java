package dao;

import dbFactory.DBConnector;
import entitys.Category;
import ui.Ui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.InputLiterals.*;

public class CategoryDAO {

    private static final String insertCategory = "insert into categories (category_name) values (?)";

    private static final String getCategoryByName = "select * from categories where category_name = ?";

    private static final String getCategoryById = "select * from categories where category_id = ?";

    private static final String getAllCategories = "SELECT * FROM shop_db.categories order by category_id";

    public static void getCategoryByName(DBConnector connector, Category category, String categoryName) {
        final PreparedStatement statement = connector.getPrepareStatement(getCategoryByName);
        try {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                category.setCategoryId(resultSet.getInt(CATEGORY_ID));
                category.setCategoryName(resultSet.getString(CATEGORY_NAME));
            } else {
                if (Ui.noRecordInDB(CATEGORIES)) {
                    category.setCategoryName(categoryName);
                    insertCategory(connector, category);
                    getCategoryByName(connector, category, categoryName);
                }
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Category getCategoryById(DBConnector connector, Category category, int categoryId) {
        final PreparedStatement statement = connector.getPrepareStatement(getCategoryById);
        try {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            category.setCategoryId(resultSet.getInt(CATEGORY_ID));
            category.setCategoryName(resultSet.getString(CATEGORY_NAME));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
        return category;
    }

    public static void insertCategory(DBConnector connector, Category category) {
        final PreparedStatement statement = connector.getPrepareStatement(insertCategory);
        try {
            statement.setString(1, category.getCategoryName());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void readCategories(DBConnector connector, Category category) {
        ResultSet resultSet = connector.executeQuery(getAllCategories);
        try{
            while (resultSet.next()){
                category.setCategoryId(resultSet.getInt(CATEGORY_ID));
                category.setCategoryName(resultSet.getString(CATEGORY_NAME));
                System.out.println(category);
            }
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
