package entitys;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private User user;
    private Timestamp date;

    private int orderStatus;

    private static String[] allStatusNames = {"open",
            "in progress",
            "completed",
            "canceled"
    };



    public String getStatusName() {
        return allStatusNames[orderStatus - 1];
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return orderId + "\t" + user.getFirstName() + "\t" + user.getLastName() + "\t" + user.getAddress() + "\t" + date + "\t" + getStatusName();
    }
}
