package ee.ut.math.tvt.salessystem.dataobjects;
import javax.persistence.*;
import java.util.Set;


/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
@Entity
@Table(name = "SOLDITEM")
public class SoldItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "STOCKITEM_ID", nullable = false)
    private StockItem stockItem;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Transient
    private double price;

    @Transient
    private double sum;

    @Transient
    private String name;

    @ManyToMany
    @JoinTable(
            name = "SOLDITEM_TO_ORDER",
            joinColumns = @JoinColumn(name = "SOLDITEM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    )
    private Set<Order> Orders;



    public SoldItem() {
    }

    public SoldItem(StockItem stockItem, int quantity) {
        this.stockItem = stockItem;
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.sum = price * ((double) quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    @Override
    public String toString() {
        return String.format("SoldItem{id=%d, name='%s'}", id, name);
    }
}