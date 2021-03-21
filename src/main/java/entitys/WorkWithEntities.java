package entitys;

import dao.*;
import dbFactory.DBConnector;
import ui.Ui;

import java.sql.Timestamp;
import java.util.Date;

import static app.InputLiterals.*;

public final class WorkWithEntities {


    private boolean correctCreation = true;


    public WorkWithEntities() {
    }

    public WorkWithEntities(DBConnector connector, Ui ui, String crudCommand, User user) {
        if (crudCommand.equals(CREATE)) {
            createUser(connector, ui, user);
        }
        if (crudCommand.equals(READ)) {
            System.out.println("user_id\t first_name\t last_name\t address");
            UserDAO.getAllUsers(connector, user);
        }
        if (crudCommand.equals(UPDATE)) {
            updateUser(connector, ui, user);
        }
        if (crudCommand.equals(DELETE)) {
            deleteUser(connector, ui, user);
        }
    }

    public WorkWithEntities(DBConnector connector, Ui ui, String crudCommand, Category category) {
        if (crudCommand.equals(CREATE)) {
            createCategory(connector, ui, category);
        }
        if (crudCommand.equals(READ)) {
            System.out.println("category_id\t category_name");
            CategoryDAO.readCategories(connector, category);
        }
        if (crudCommand.equals(UPDATE)) {
            System.out.println("Такой функционал не заложен в приложение");
        }
        if (crudCommand.equals(DELETE)) {
            System.out.println("Такой функционал не заложен в приложение");
        }
    }

    public WorkWithEntities(DBConnector connector, Ui ui, String crudCommand, Product product, Category category) {
        if (crudCommand.equals(CREATE)) {
            creteProduct(connector, ui, product, category);
        }
        if (crudCommand.equals(READ)) {
            System.out.println("product_id\t category_name\t product_name\t price");
            ProductDAO.readProducts(connector, product, category);
        }
        if (crudCommand.equals(UPDATE)) {
            updateProduct(connector, ui, product, category);
        }
        if (crudCommand.equals(DELETE)) {
            System.out.println("Такой функционал не заложен в приложение");
        }
    }

    public WorkWithEntities(DBConnector connector, Ui ui, String crudCommand, Order order, User user, Category category, OrderMap orderMap, Product product) {
        if (crudCommand.equals(CREATE)) {
            createOrder(connector, ui, order, user);
            createOrderMap(connector, ui, orderMap, order, category);
        }
        if (crudCommand.equals(READ)) {
            if (ui.getOneOrder()) {
                updateOrder(connector, ui, order, user);
                orderMap.setOrder(order);
                orderMap = getOrderMapFromDB(connector, orderMap, category);
                System.out.println("order_id\t date\t first_name\t last_name\t category_name\t product_name\t quantity\t total_price\t address\t status");
                System.out.println(orderMap);
            } else {
                System.out.println("order_id\t date\t first_name\t last_name\t category_name\t product_name\t quantity\t total_price\t address\t status");
                OrderDAO.getAllOrdersInformation(connector, order, user, category, orderMap, product);
            }
        }
        if (crudCommand.equals(UPDATE)) {
            updateOrder(connector, ui, order, user);
            if (order.getDate() != null) {
                updateOrderMap(connector, ui, orderMap, order, category);
            }
        }
        if (crudCommand.equals(DELETE)) {
            System.out.println("Такой функционал не заложен в приложение");
        }
    }

    private void deleteUser(DBConnector connector, Ui ui, User user) {
        do {
            System.out.println("Введите id пользователя:");
            int userId;
            while ((userId = ui.setIntegerField(USER_ID)) == 1) {
                System.out.println("Этот id зарезервирован и не может быть использован!");
            }
            UserDAO.getUserWithoutCreation(connector, user, userId);
        } while (user.getFirstName() == null);

        OrderDAO.userWasDeleted(connector, user.getUserId());
        UserDAO.deleteUser(connector, user.getUserId());
    }


    private void updateOrderMap(DBConnector connector, Ui ui, OrderMap orderMap, Order order, Category category) {
        orderMap.setOrder(order);
        orderMap = getOrderMapFromDB(connector, orderMap, category);
        System.out.println("order_id\t date\t first_name\t last_name\t category_name\t product_name\t quantity\t total_price\t address\t status");
        System.out.println(orderMap);
        if (ui.changeField(ORDER_STATUS)) {
            order = orderMap.getOrder();
            order.setOrderStatus(ui.setStatusField());
            OrderDAO.updateOrder(connector, order);
        }
        while (ui.changeField("содержимое заказа")) {
            if (ui.addProduct()) {
                do {
                    Product product = new Product();
                    product = getProductFromDB(connector, ui.setStringField(PRODUCT_NAME), product, category);
                    if (product.getProductName() != null) {
                        orderMap.putProductCount(product, ui.setIntegerField(QUANTITY));
                    }
                } while (ui.oneMoreProductInBasket());
                OrderMapDAO.insertOrderMap(connector, orderMap);
            }

            while (ui.updateProductQuantity()) {
                Product product = new Product();
                product = getProductFromDB(connector, ui.setStringField(PRODUCT_NAME), product, category);
                if ((product.getProductName() != null) && OrderMapDAO.containProduct(connector, order.getOrderId(), product.getProductId())) {
                    OrderMapDAO.updateProductQuantity(connector, order.getOrderId(), product.getProductId(), ui.setIntegerField(QUANTITY));
                } else {
                    System.out.println("Такого продукта в заказе нет!");
                }
            }

            while (ui.deleteProductFromOrder()) {
                Product product = new Product();
                product = getProductFromDB(connector, ui.setStringField(PRODUCT_NAME), product, category);
                if (product.getProductName() != null) {
                    OrderMapDAO.deleteProductFromOrder(connector, order.getOrderId(), product.getProductId());
                }
            }
        }

        if (OrderMapDAO.orderMapCheck(connector, order.getOrderId())) {
            order.setOrderStatus(4);
            OrderDAO.updateOrder(connector, order);
        }
    }

    private void createOrderMap(DBConnector connector, Ui ui, OrderMap orderMap, Order order, Category category) {
        if (!correctCreation) {
            return;
        }
        orderMap.setOrder(order);
        do {
            Product product = new Product();
            product = getProductFromDB(connector, ui.setStringField(PRODUCT_NAME), product, category);
            if (product.getProductName() != null) {
                orderMap.putProductCount(product, ui.setIntegerField(QUANTITY));
            }
        } while (ui.oneMoreProductInBasket());

        OrderMapDAO.insertOrderMap(connector, orderMap);
    }

    private void updateOrder(DBConnector connector, Ui ui, Order order, User user) {
        order = getOrderFromDB(connector, ui.setIntegerField(ORDER_ID), order, user);
        if (order.getDate() == null) {
            return;
        } else {
            order.setUser(getUserFromDB(connector, user.getUserId(), user));
        }
    }


    private void createOrder(DBConnector connector, Ui ui, Order order, User user) {
        Date date = new Date();
        Timestamp rightDate = new Timestamp(date.getTime() / 1000 * 1000);
        order.setDate(rightDate);
        order.setUser(getUserFromDB(connector, ui.setIntegerField(USER_ID), user, ui));
        if (order.getUser().getFirstName() == null) {
            correctCreation = false;
        } else {
            OrderDAO.insertOrder(connector, order);
            OrderDAO.getOrder(connector, order);
        }
    }

    private void updateProduct(DBConnector connector, Ui ui, Product product, Category category) {
        product = getProductFromDB(connector, ui.setIntegerField(PRODUCT_ID), product, category);
        if (product.getProductName() == null) {
            return;
        } else {
            boolean wasChanged = false;
            System.out.println(product);
            if (ui.changeField(PRODUCT_NAME)) {
                product.setProductName(ui.setStringField(PRODUCT_NAME));
                wasChanged = true;
            }
            if (ui.changeField(PRODUCT_PRICE)) {
                product.setPrice(ui.setBigDecimalField(PRODUCT_PRICE));
                wasChanged = true;
            }
            if (ui.changeField(PRODUCT_CATEGORY)) {
                product.setCategory(getCategoryFromDB(connector, ui.setStringField(CATEGORY_NAME), category));
                wasChanged = true;
            }
            if (wasChanged) {
                ProductDAO.updateProduct(connector, product);
                System.out.println("Продукт изменен успешно!");
            } else {
                System.out.println("Вы не внесли изменений!");
            }
        }
    }

    private void creteProduct(DBConnector connector, Ui ui, Product product, Category category) {
        product.setProductName(ui.setStringField(PRODUCT_NAME));
        product.setPrice(ui.setBigDecimalField(PRODUCT_PRICE));
        product.setCategory(getCategoryFromDB(connector, ui.setStringField(CATEGORY_NAME), category));
        if (product.getCategory().getCategoryName() == null) {
            correctCreation = false;
        } else {
            ProductDAO.insertProduct(connector, product);
        }
    }

    private void createCategory(DBConnector connector, Ui ui, Category category) {
        category.setCategoryName(ui.setStringField(CATEGORY_NAME));
        CategoryDAO.insertCategory(connector, category);
    }

    private void updateUser(DBConnector connector, Ui ui, User user) {
        int userId;
        while ((userId = ui.setIntegerField(USER_ID)) == 1) {
            System.out.println("Этот id зарезервирован и не может быть использован!");
        }
        user = getUserFromDB(connector, userId, user);
        if (user.getFirstName() == null) {
            return;
        } else {
            boolean wasChanged = false;
            System.out.println(user);
            if (ui.changeField(USER_FIRST_NAME)) {
                user.setFirstName(ui.setStringField(USER_FIRST_NAME));
                wasChanged = true;
            }
            if (ui.changeField(USER_LAST_NAME)) {
                user.setLastName(ui.setStringField(USER_LAST_NAME));
                wasChanged = true;
            }
            if (ui.changeField(USER_ADDRESS)) {
                user.setAddress(ui.setStringField(USER_ADDRESS));
                wasChanged = true;
            }
            if (wasChanged) {
                UserDAO.updateUser(connector, user);
                System.out.println("Пользователь изменен успешно!");
            } else {
                System.out.println("Вы не внесли изменений!");
            }
        }

    }

    private void createUser(DBConnector connector, Ui ui, User user) {
        user.setFirstName(ui.setStringField(USER_FIRST_NAME));
        user.setLastName(ui.setStringField(USER_LAST_NAME));
        user.setAddress(ui.setStringField(USER_ADDRESS));
        UserDAO.insertUser(connector, user);
    }

    private OrderMap getOrderMapFromDB(DBConnector connector, OrderMap orderMap, Category category) {
        OrderMapDAO.getOrderMap(connector, orderMap, category);
        return orderMap;
    }

    private Order getOrderFromDB(DBConnector connector, int setIntegerField, Order order, User user) {
        OrderDAO.getOrder(connector, order, user, setIntegerField);
        return order;
    }

    private Product getProductFromDB(DBConnector connector, int setIntField, Product product, Category category) {
        ProductDAO.getProductById(connector, product, category, setIntField);
        return product;
    }

    private Product getProductFromDB(DBConnector connector, String setStringField, Product product, Category category) {
        ProductDAO.getProductByName(connector, product, category, setStringField);
        return product;
    }

    private User getUserFromDB(DBConnector connector, int setIntegerField, User user) {
        UserDAO.getUserWithoutCreation(connector, user, setIntegerField);
        return user;
    }

    private User getUserFromDB(DBConnector connector, Integer setIntegerField, User user, Ui ui) {
        UserDAO.getUser(connector, user, setIntegerField, ui);
        return user;
    }

    private Category getCategoryFromDB(DBConnector connector, String setStringField, Category category) {
        CategoryDAO.getCategoryByName(connector, category, setStringField);
        return category;
    }

    public boolean isCorrectCreation() {
        return correctCreation;
    }
}
