package ee.ut.math.tvt.salessystem.dataobjects;
import org.hibernate.annotations.Cascade;

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
    @Column(name = "SOLDITEM_ID")
    private Long id;


    @JoinColumn(name = "STOCKITEM_SOLDITEM_ID")
    private Long solditem_stockitem_id;

    @OneToOne
    private StockItem stockItem;

    @Column(name = "SOLDITEM_QUANTITY")
    private Integer quantity;

    @Column(name = "SOLDITEM_PRICE")
    private double price;

    @Column(name = "SOLDITEM_SUM")
    private double sum;

    @Column(name = "SOLDITEM_NAME")
    private String name;

    /*
    @ManyToMany
    @JoinTable(
            name = "SOLDITEM_TO_ORDER",
            joinColumns = @JoinColumn(name = "SOLDITEM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    )
     */

    @ManyToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "PURCHASE_ID")
    private Set<Purchase> Purchases;



    public SoldItem() {
    }

    public SoldItem(StockItem stockItem, int quantity, Long solditem_stockitem_id) {
        this.stockItem = stockItem;
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.sum = price * ((double) quantity);
        this.solditem_stockitem_id = solditem_stockitem_id;
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