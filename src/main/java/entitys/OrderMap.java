package entitys;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class OrderMap {
    private HashMap<Product, Integer> productCount = new HashMap<>();

    private Order order;

    public void putProductCount(Product product, int value) {
        productCount.put(product, value);
    }


    public HashMap<Product, Integer> getProductCount() {
        return productCount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        String toString = "";
        for (Map.Entry<Product, Integer> entry : productCount.entrySet()) {
            toString +=  "\n" + order.getOrderId() + "\t" + order.getDate() + "\t" + order.getUser().getFirstName() +
                    "\t" + order.getUser().getLastName() + "\t" + entry.getKey().getCategory().getCategoryName() +
                    "\t" + entry.getKey().getProductName() + "\t" + entry.getValue() +
                    "\t" + entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())) +
                    "\t" + order.getUser().getAddress() + "\t" + order.getStatusName();
        }

        return toString;
    }
}
