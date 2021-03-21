package ui;

import java.math.BigDecimal;

import static app.InputLiterals.*;

public interface CorrectInputChecking {

    default boolean tableInputNotCorrect(String table) {
        if (table.equals(PRODUCTS) || table.equals(USERS) || table.equals(ORDERS) ||
                table.equals(CATEGORIES)|| table.equals(EXIT)) {
            return false;
        } else return true;
    }

    default boolean crudInputNotCorrect(String command) {
        if (command.equals(CREATE) || command.equals(UPDATE) || command.equals(DELETE) ||
                command.equals(READ)) {
            return false;
        } else return true;
    }

    default boolean stringInputNotCorrect(String str) {
        if (!str.equals("")) {
            return false;
        } else return true;
    }

    default boolean bigDecimalInputNotCorrect(String str) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            return false;
        } catch (NumberFormatException exc) {
            return true;
        }
    }

    default boolean integerInputNotCorrect(String str) {
        try {
            Integer integer = Integer.parseInt(str);
            return false;
        } catch (NumberFormatException exc) {
            return true;
        }
    }

    default boolean statusInputNotCorrect(String str){
        if (str.equals("1") || str.equals("2") || str.equals("3") || str.equals("4")) {
            return false;
        } else return true;
    }

    static boolean yesNoAnswerNotCorrect(String str) {
        if (str.equals("y") || str.equals("Y") || str.equals("n") || str.equals("N")) {
            return false;
        } else return true;
    }
}
