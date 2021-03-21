package dao;

import dbFactory.DBConnector;
import entitys.*;
import ui.Ui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static app.InputLiterals.*;

public class OrderDAO {

    private static final String insertOrder = "insert into orders (user_id, date) values (?,?)";

    private static final String getOrder = "select * from orders where user_id = ? and date = ?";

    private static final String getOrderById = "select * from orders where order_id = ?";

    private static final String updateOrder = "update orders set status = ? where order_id = ?";

    private static final String getAllOrdersInformation = "select * from orders \n" +
            " inner join order_status on orders.status = order_status.status_id \n" +
            "    inner join orders_map on orders.order_id = orders_map.order_id\n" +
            "    inner join users on orders.user_id = users.user_id\n" +
            "    inner join products on products.product_id = orders_map.product_id\n" +
            "    inner join categories on categories.category_id = products.category order by date";

    private static final String userWasDeleted = "update orders set user_id = '1' where user_id = ?";

    private static final String changeDeletedUsersOrders = "update orders set status = '4' where (status = '1' or status = '2') and user_id = '1'";

    public static void updateOrder(DBConnector connector, Order order) {
        final PreparedStatement statement = connector.getPrepareStatement(updateOrder);
        try {
            statement.setInt(1, order.getOrderStatus());
            statement.setInt(2, order.getOrderId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }

    }

    public static void insertOrder(DBConnector connector, Order order){
        final PreparedStatement statement = connector.getPrepareStatement(insertOrder);
        try {
            statement.setInt(1, order.getUser().getUserId());
            statement.setTimestamp(2, order.getDate());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void getOrder(DBConnector connector, Order order) {
        final PreparedStatement statement = connector.getPrepareStatement(getOrder);
        try {
            statement.setInt(1, order.getUser().getUserId());
            statement.setTimestamp(2, order.getDate());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            order.setOrderId(resultSet.getInt(ORDER_ID));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void getOrder(DBConnector connector, Order order, User user, int orderId) {
        final PreparedStatement statement = connector.getPrepareStatement(getOrderById);
        try {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                order.setOrderId(resultSet.getInt(ORDER_ID));
                user.setUserId(resultSet.getInt(USER_ID));
                order.setDate(resultSet.getTimestamp(DATE));

                order.setOrderStatus(resultSet.getInt(ORDER_STATUS));

            }else{
                System.out.println("Заказа с таким id не существует!");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void getAllOrdersInformation(DBConnector connector, Order order, User user, Category category, OrderMap orderMap, Product product){
        ResultSet resultSet = connector.executeQuery(getAllOrdersInformation);
        try{
            while(resultSet.next()){
                    order.setOrderId(resultSet.getInt(ORDER_ID));
                    order.setOrderStatus(resultSet.getInt(ORDER_STATUS));
                    order.setDate(resultSet.getTimestamp(DATE));
                    user.setFirstName(resultSet.getString(USER_FIRST_NAME));
                    user.setLastName(resultSet.getString(USER_LAST_NAME));
                    user.setAddress(resultSet.getString(USER_ADDRESS));
                    user.setUserId(resultSet.getInt(USER_ID));
                    order.setUser(user);
                    category.setCategoryId(resultSet.getInt(CATEGORY_ID));
                    category.setCategoryName(resultSet.getString(CATEGORY_NAME));
                    product.setProductId(resultSet.getInt(PRODUCT_ID));
                    product.setProductName(resultSet.getString(PRODUCT_NAME));
                    product.setCategory(category);
                    product.setPrice(resultSet.getBigDecimal(PRODUCT_PRICE));
                    orderMap.setOrder(order);
                    orderMap.putProductCount(product, resultSet.getInt(QUANTITY));
                System.out.print(orderMap);
            }
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void userWasDeleted(DBConnector connector, int userId) {
        final PreparedStatement statement = connector.getPrepareStatement(userWasDeleted);
        try{
            statement.setInt(1, userId);
            statement.executeUpdate();
            connector.execute(changeDeletedUsersOrders);
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
