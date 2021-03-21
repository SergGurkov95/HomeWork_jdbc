package dbFactory;

import static dbFactory.ConnectionConstants.*;

public class DBFactory {
    public static final String CREATE_DB = "create schema IF NOT EXISTS `shop_db`";

    public static final String CREATE_TABLE_PRODUCTS = "create table IF NOT EXISTS `products`(\n" +
            "    `product_id` int not null auto_increment,\n" +
            "    `product_name` varchar(50) not null,\n" +
            "    `category` int not null,\n" +
            "    `price` decimal(10,2) not null,\n" +
            "primary key (`product_id`)," +
            "constraint `fk_category_id`\n" +
            "    foreign key (`category`)\n" +
            "    references categories(`category_id`)\n" +
            "    on delete no action\n" +
            "    on update no action)";

    public static final String CREATE_TABLE_CATEGORY = "create table IF NOT EXISTS `categories`(\n" +
            "   `category_id` int not null auto_increment,\n" +
            "   `category_name` varchar(50) not null,\n" +
            "primary key(`category_id`)," +
            "unique index `category_name_UNIQUE` (`category_name` asc) visible)";

    public static final String CREATE_TABLE_USERS = "create table IF NOT EXISTS `users`(\n" +
            "    `user_id` int not null auto_increment,\n" +
            "    `first_name` varchar(50) not null,\n" +
            "    `last_name` varchar(50) not null,\n" +
            "    `address` varchar(50) not null,\n" +
            "primary key (`user_id`))" +
            "ENGINE=InnoDB";

    public static final String CREATE_TABLE_STATUS = "create table IF NOT EXISTS `order_status`(\n" +
            "    `status_id` int not null,\n" +
            "    `status_name` varchar(50) not null,\n" +
            "primary key(`status_id`))" +
            "ENGINE=InnoDB";

    public static final String CREATE_TABLE_ORDERS = "create table IF NOT EXISTS `orders`(\n" +
            "    `order_id` int not null auto_increment,\n" +
            "    `user_id` int not null,\n" +
            "    `date` timestamp not null,\n" +
            "    `status` int not null default '1',\n" +
            "primary key (`order_id`),\n" +
            "constraint `fk_user_id`\n" +
            "    foreign key (`user_id`)\n" +
            "    references users(user_id)\n" +
            "    on delete no action\n" +
            "    on update no action,\n" +
            "constraint `fk_status_id`\n" +
            "    foreign key (`status`)\n" +
            "    references order_status(status_id)\n" +
            "    on delete no action\n" +
            "    on update no action)\n";

    public static final String CREATE_TABLE_ORDERS_MAP = "create table IF NOT EXISTS `orders_map`(\n" +
            "    `order_id` int not null,\n" +
            "    `product_id` int not null,\n" +
            "    `quantity` int not null,\n" +
            "primary key (`order_id`, `product_id`),\n" +
            "constraint `fk_order_id` \n" +
            "    foreign key (`order_id`)\n" +
            "    references orders(order_id)\n" +
            "    on delete no action\n" +
            "    on update no action,\n" +
            "constraint `fk_product_id` \n" +
            "    foreign key (`product_id`)\n" +
            "    references products(product_id)\n" +
            "    on delete no action\n" +
            "    on update no action)";

    private static final String DELETED_USER = "insert into users (user_id, first_name, last_name, address)\n" +
            "select * from (select '1', 'user', 'was', 'deleted') as tmp\n" +
            "where not exists (select user_id from users where user_id = '1')";

    private static final String STATUS_OPEN = "insert into order_status (status_id, status_name)\n" +
            "select * from (select '1', 'open') as tmp\n" +
            "where not exists (select status_id from order_status where status_id = '1')";

    private static final String STATUS_IN_PROGRESS = "insert into order_status (status_id, status_name)\n" +
            "select * from (select '2', 'in progress') as tmp\n" +
            "where not exists (select status_id from order_status where status_id = '2')";

    private static final String STATUS_COMPLETED = "insert into order_status (status_id, status_name)\n" +
            "select * from (select '3', 'completed') as tmp\n" +
            "where not exists (select status_id from order_status where status_id = '3')";

    private static final String STATUS_CANCELED = "insert into order_status (status_id, status_name)\n" +
            "select * from (select '4', 'canceled') as tmp\n" +
            "where not exists (select status_id from order_status where status_id = '4')";

    public static void checkDB(DBConnector connector) {
        connector.execute(CREATE_DB);
        connector.execute(USE_DB);
        connector.execute(CREATE_TABLE_CATEGORY);
        connector.execute(CREATE_TABLE_PRODUCTS);
        connector.execute(CREATE_TABLE_USERS);
        connector.execute(DELETED_USER);
        connector.execute(CREATE_TABLE_STATUS);
        insertStatus(connector);
        connector.execute(CREATE_TABLE_ORDERS);
        connector.execute(CREATE_TABLE_ORDERS_MAP);
    }


    private static void insertStatus(DBConnector connector) {
        connector.execute(STATUS_OPEN);
        connector.execute(STATUS_IN_PROGRESS);
        connector.execute(STATUS_COMPLETED);
        connector.execute(STATUS_CANCELED);
    }

}
