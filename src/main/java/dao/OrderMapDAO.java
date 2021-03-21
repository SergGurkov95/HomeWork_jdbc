package dao;

import dbFactory.DBConnector;
import entitys.Category;
import entitys.OrderMap;
import entitys.Product;
import ui.Ui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static app.InputLiterals.*;

public class OrderMapDAO {
    private static final String insertOrderMap = "insert into orders_map (order_id, product_id, quantity) values(?,?,?)";

    private static final String getOrderMap = "select * from orders_map where order_id = ?";

    private static final String containProduct = "select * from orders_map where order_id = ? and product_id = ?";

    private static final String deleteProductFromOrderMap = "delete from orders_map where order_id = ? and product_id = ?";

    private static final String updateProductQuantity = "update orders_map set quantity = ? WHERE order_id = ? and product_id = ?";

    public static void updateProductQuantity(DBConnector connector, int orderId, int productId, int newQuantity) {
        final PreparedStatement statement = connector.getPrepareStatement(updateProductQuantity);
        try{
            statement.setInt(1, newQuantity);
            statement.setInt(2, orderId);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void deleteProductFromOrder(DBConnector connector, int orderId, int productId) {
        final PreparedStatement statement = connector.getPrepareStatement(deleteProductFromOrderMap);
        try{
            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.executeUpdate();
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static boolean orderMapCheck(DBConnector connector, int orderId) {
        final PreparedStatement statement = connector.getPrepareStatement(getOrderMap);
        try {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next()){
                return true;
            }else return false;
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static boolean containProduct(DBConnector connector, int orderId, int productId) {
        final PreparedStatement statement = connector.getPrepareStatement(containProduct);
        try {
            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else return false;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
    public static OrderMap getOrderMap(DBConnector connector, OrderMap orderMap, Category category) {
        final PreparedStatement statement = connector.getPrepareStatement(getOrderMap);
        try{
            statement.setInt(1, orderMap.getOrder().getOrderId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                orderMap.putProductCount(ProductDAO.getProductById(connector, product, category, resultSet.getInt(PRODUCT_ID)), resultSet.getInt(QUANTITY));
            }
            return orderMap;
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void insertOrderMap(DBConnector connector, OrderMap orderMap) {
        final PreparedStatement statement = connector.getPrepareStatement(insertOrderMap);

        try {
            for (Map.Entry<Product, Integer> entry : orderMap.getProductCount().entrySet()) {
                statement.setInt(1, orderMap.getOrder().getOrderId());
                statement.setInt(2, entry.getKey().getProductId());
                statement.setInt(3, entry.getValue());
                statement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
