package ee.ut.math.tvt.salessystem.dataobjects;
import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PURCHASE")
public class Purchase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PURCHASE_ID", nullable = false)
    private Long id;
    @Column(name = "DATE")
    private LocalDate date;

    @JoinTable(name="SOLDITEM_TO_PURCHASE",
        inverseJoinColumns = @JoinColumn(name = "solditem_ID", referencedColumnName = "SOLDITEM_ID"),
        joinColumns = @JoinColumn(name = "purchase_ID", referencedColumnName = "PURCHASE_ID"
    )
    )

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SoldItem> items;

    @Column(name = "TOTAL")
    private double total;


    public Purchase(Long id, LocalDate date, double total, Set<SoldItem> items) {
        this.id = id;
        this.date = date;
        this.total =total;
        this.items = items;
    }

    public Purchase() {

    }


    public Long getid() {
        return id;
    }


    public LocalDate getDate() {
        return date;
    }


    public double getTotal() {
        for (SoldItem item : items) {
            total += item.getPrice()*item.getQuantity();
        }
        return total;
    }



    public List<SoldItem> getItems() {
        return (List<SoldItem>) items;
   }
}