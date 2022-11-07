package ee.ut.math.tvt.salessystem.dataobjects;
import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "ORDER")
public class Order {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Column(name = "DATE")
    private String date;
    @Column(name = "TIME")
    private String time;
    @Column(name = "TOTAL")
    private double total;
    @Transient
    private List<SoldItem> items;

    public Order(String date, String time, double total, List<SoldItem> items) {
//        this.id = id;
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