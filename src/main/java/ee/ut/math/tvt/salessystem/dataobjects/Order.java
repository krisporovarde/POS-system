package ee.ut.math.tvt.salessystem.dataobjects;

import java.util.List;

public class Order {

    private String date;
    private String time;
    private double total;
    private List<SoldItem> items;

    public Order(String date, String time, double total, List<SoldItem> items) {
        this.date = date;
        this.time = time;
        this.total =total;
        this.items = items;
    }

    public String getDate() {
        return date;
    }


    public String getTime() {
        return time;
    }

    public double getTotal() {
        return total;
    }

    public List<SoldItem> getItems() {
        return items;
    }
}
