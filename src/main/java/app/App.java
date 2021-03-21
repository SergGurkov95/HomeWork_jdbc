package app;

import dbFactory.DBConnector;
import dbFactory.DBFactory;
import entitys.*;
import ui.Ui;

import java.sql.Timestamp;
import java.util.Date;

import static app.InputLiterals.*;


public class App {

    private static boolean AppIsRunning = true;


    public static void main(String[] args) {

        DBConnector connector = new DBConnector();
        DBFactory.checkDB(connector);
        Ui ui = new Ui();
        WorkWithEntities entity = new WorkWithEntities();

        String crudCommand;
        String table;

        User user;
        Category category;
        Order order;
        OrderMap orderMap;
        Product product;



        while (AppIsRunning) {
            while (true) {

                user = new User();
                category = new Category();
                order = new Order();
                orderMap = new OrderMap();
                product = new Product();

                table = ui.choiceTable();

                if (appState(table)) {
                    break;
                }

                crudCommand = ui.choiceCRUD();

                switch (table) {

                    case (USERS):
                        entity = new WorkWithEntities(connector, ui, crudCommand, user);
                        break;
                    case (PRODUCTS):
                        entity = new WorkWithEntities(connector, ui, crudCommand, product, category);
                        break;
                    case (CATEGORIES):
                        entity = new WorkWithEntities(connector, ui, crudCommand, category);
                        break;
                    case (ORDERS):
                        entity = new WorkWithEntities(connector, ui, crudCommand, order, user, category, orderMap, product);
                }

                if (!entity.isCorrectCreation()) {
                    System.out.println("Объект создан неверно!\nПопробуйте еще раз!");
                    break;
                }
            }
        }
        connector.close();
    }


    public static boolean appState(String command) {
        if (command.equals(EXIT)) {
            AppIsRunning = false;
            return true;
        } else return false;
    }
}