package dao;

import dbFactory.DBConnector;
import entitys.User;
import ui.Ui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static app.InputLiterals.*;

public class UserDAO {

    private static final String insertUser = "insert into users (first_name, last_name, address) values (?,?,?)";

    private static final String getUser = "select * from users where user_id = ?";

    private static final String updateUser = "update users set first_name = ?, last_name = ?, address = ? where user_id = ?";

    private static final String getAllUsers = "select * from users order by user_id";

    private static final String deleteUser = "delete from users where user_id = ?";

    public static void getUserWithoutCreation(DBConnector connector, User user, int userId) {
        final PreparedStatement statement = connector.getPrepareStatement(getUser);
        try {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt(USER_ID));
                user.setFirstName(resultSet.getString(USER_FIRST_NAME));
                user.setLastName(resultSet.getString(USER_LAST_NAME));
                user.setAddress(resultSet.getString(USER_ADDRESS));
            } else {
                System.out.println("Пользователь с таким id не существует!");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void getUser(DBConnector connector, User user, Integer userId, Ui ui) {
        final PreparedStatement statement = connector.getPrepareStatement(getUser);
        try {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt(USER_ID));
                user.setFirstName(resultSet.getString(USER_FIRST_NAME));
                user.setLastName(resultSet.getString(USER_LAST_NAME));
                user.setAddress(resultSet.getString(USER_ADDRESS));
            } else {
                if (Ui.noRecordInDB(USERS)) {
                    user.setFirstName(ui.setStringField(USER_FIRST_NAME));
                    user.setLastName(ui.setStringField(USER_LAST_NAME));
                    user.setAddress(ui.setStringField(USER_ADDRESS));
                    insertUser(connector, user);
                }
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void insertUser(DBConnector connector, User user) {
        final PreparedStatement statement = connector.getPrepareStatement(insertUser);
        try {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }

    }

    public static void updateUser(DBConnector connector, User user) {
        final PreparedStatement statement = connector.getPrepareStatement(updateUser);
        try {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getAddress());
            statement.setInt(4, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void getAllUsers(DBConnector connector, User user){
        ResultSet resultSet = connector.executeQuery(getAllUsers);
        try{
            while (resultSet.next()){
                user.setUserId(resultSet.getInt(USER_ID));
                user.setFirstName(resultSet.getString(USER_FIRST_NAME));
                user.setLastName(resultSet.getString(USER_LAST_NAME));
                user.setAddress(resultSet.getString(USER_ADDRESS));
                System.out.println(user);
            }
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static void deleteUser(DBConnector connector, int userId) {
        final PreparedStatement statement = connector.getPrepareStatement(deleteUser);
        try{
            statement.setInt(1, userId);
            statement.executeUpdate();
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
