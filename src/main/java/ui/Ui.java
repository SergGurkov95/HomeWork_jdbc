package ui;

import entitys.Product;

import java.math.BigDecimal;
import java.util.Scanner;

import static app.InputLiterals.*;

public class Ui implements CorrectInputChecking {

    private static Scanner scanner = new Scanner(System.in);
    private String command;
    private String table;
    // private String stringField;


    public String choiceTable() {
        System.out.println("\nВыберите таблицу с которой хотите взаимодействовать или закройте приложение(\"" +
                PRODUCTS + "\", \"" +
                USERS + "\", \"" +
                CATEGORIES + "\", \"" +
                ORDERS + "\", \"" +
                EXIT + "\"):");
        table = Ui.readInput();

        if (tableInputNotCorrect(table)) {
            System.out.println("Неверный ввод!");
            return choiceTable();
        }
        return table;
    }

    public String choiceCRUD() {
        System.out.println("\nДля создания записи введите \"" + CREATE + "\";" +
                "\nДля изменения записи введите \"" + UPDATE + "\";" +
                "\nДля удаления записи введите \"" + DELETE + "\";" +
                "\nДля отображения информации по записям введите \"" + READ + "\":");
        command = Ui.readInput();

        if (crudInputNotCorrect(command)) {
            System.out.println("Неверный ввод!");
            return choiceCRUD();
        }
        return command;
    }

    public String setStringField(String fieldName) {
        System.out.println("\nВведите " + fieldName + ":");
        String str = readInput();
        if (stringInputNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return setStringField(fieldName);
        }
        return str;
    }

    public BigDecimal setBigDecimalField(String fieldName) {
        System.out.println("\nВведите " + fieldName + ":");
        String str = readInput();
        if (bigDecimalInputNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return setBigDecimalField(fieldName);
        }
        return new BigDecimal(str);
    }

    public int setIntegerField(String fieldName) {
        System.out.println("\nВведите " + fieldName + ":");
        String str = readInput();
        if (integerInputNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return setIntegerField(fieldName);
        }
        return Integer.parseInt(str);
    }

    public static boolean noRecordInDB(String table) {
        System.out.println("В таблице " + table + " такой записи нет!");
        System.out.println("\nСоздать(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return noRecordInDB(table);
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }


    public boolean oneMoreProductInBasket() {
        System.out.println("Добавить к заказу еще 1 продукт(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return oneMoreProductInBasket();
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }

    public boolean updateProductQuantity() {
        System.out.println("Изменить количество продукта?(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return addProduct();
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }

    public boolean deleteProductFromOrder() {
        System.out.println("Удалить продукт из заказа(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return addProduct();
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }

    public boolean getOneOrder() {
        System.out.println("Получить информацию по одному заказу(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return addProduct();
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }

    public boolean addProduct() {
        System.out.println("Добавить продукт к заказу(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return addProduct();
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }

    public boolean changeField(String fieldName) {
        System.out.println("Изменить " + fieldName + "(y/n)?");
        String str = readInput();
        if (CorrectInputChecking.yesNoAnswerNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return changeField(fieldName);
        }
        if (str.equals("y") || str.equals("Y")) {
            return true;
        } else return false;
    }

    public static String readInput() {
        return scanner.nextLine();
    }

    public int setStatusField() {
        System.out.println("\nВведите " + ORDER_STATUS + ":");
        String str = readInput();
        if (statusInputNotCorrect(str)) {
            System.out.println("Неверный ввод!");
            return setStatusField();
        }
        return Integer.parseInt(str);
    }
}
