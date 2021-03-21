package dao;

import dbFactory.DBConnector;
import entitys.Category;
import entitys.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.InputLiterals.*;

public class ProductDAO {

    private static final String insertProduct = "insert into products (product_name, category, price) values (?,?,?)";

    private static final String getProductByName = "select * from products where product_name = ?";

    private static final String getProductById = "select * from products where product_id = ?";

    private static final String updateProduct = "update products set product_name = ?, category = ?, price = ? where product_id = ?";

    private static final String readAllProducts = "select product_id, category_name, category_id, product_name, price from products " +
            "inner join categories on products.category = categories.category_id order by product_id";


    public static Product getProductById(DBConnector connector, Product product, Category category, int productId){
        final  PreparedStatement statement = connector.getPrepareStatement(getProductById);
        try{
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                product.setProductId(resultSet.getInt(PRODUCT_ID));
                product.setProductName(resultSet.getString(PRODUCT_NAME));
                product.setCategory(CategoryDAO.getCategoryById(connector, category, resultSet.getInt(PRODUCT_CATEGORY)));
                product.setPrice(resultSet.getBigDecimal(PRODUCT_PRICE));
            }else{
                System.out.println("Продукта с таким id не существует!");
            }
            return product;
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void getProductByName(DBConnector connector, Product product, Category category, String productName) {
        final PreparedStatement statement = connector.getPrepareStatement(getProductByName);
        try {
            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                product.setProductId(resultSet.getInt(PRODUCT_ID));
                product.setProductName(resultSet.getString(PRODUCT_NAME));
                product.setCategory(CategoryDAO.getCategoryById(connector, category, resultSet.getInt(PRODUCT_CATEGORY)));
                product.setPrice(resultSet.getBigDecimal(PRODUCT_PRICE));
            } else {
                System.out.println("Продукта с таким именем не существует!");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void insertProduct(DBConnector connector, Product product) {
        final PreparedStatement statement = connector.getPrepareStatement(insertProduct);
        try {
            statement.setString(1, product.getProductName());
            statement.setInt(2, product.getCategory().getCategoryId());
            statement.setBigDecimal(3, product.getPrice());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void updateProduct(DBConnector connector, Product product) {
        final PreparedStatement statement = connector.getPrepareStatement(updateProduct);
        try {
            statement.setString(1, product.getProductName());
            statement.setInt(2, product.getCategory().getCategoryId());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void readProducts(DBConnector connector, Product product, Category category) {
        ResultSet resultSet = connector.executeQuery(readAllProducts);
        try {
            while(resultSet.next()){
                product.setProductId(resultSet.getInt(PRODUCT_ID));
                category.setCategoryId(resultSet.getInt(CATEGORY_ID));
                category.setCategoryName(resultSet.getString(CATEGORY_NAME));
                product.setCategory(category);
                product.setProductName(resultSet.getString(PRODUCT_NAME));
                product.setPrice(resultSet.getBigDecimal(PRODUCT_PRICE));
                System.out.println(product);
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
