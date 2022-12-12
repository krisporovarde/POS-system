package ee.ut.math.tvt.salessystem.dao;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transaction;
import java.util.List;

public class HibernateSalesSystemDAO implements SalesSystemDAO {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    public HibernateSalesSystemDAO () {
        // if you get ConnectException / JDBCConnectionException then you
        // probably forgot to start the database before starting the application
        emf = Persistence.createEntityManagerFactory ("pos");
        em = emf.createEntityManager ();
    }
    public void close () {
        em.close ();
        emf.close ();
    }

    @Override
    public void savePurchase(Purchase purchase) {
        List<SoldItem> list = purchase.getItems();
        beginTransaction();
        em.merge(purchase);
        commitTransaction();
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        beginTransaction();
        if (em.find(StockItem.class, stockItem.getId()) != null) {
            StockItem item = em.find(StockItem.class, stockItem.getId());
            item.setQuantity(stockItem.getQuantity());
            item.setPrice(stockItem.getPrice());
        }else {
            em.merge(stockItem);
        }
        commitTransaction();
    }

    @Override
    public void saveSoldItem(SoldItem item) {
        beginTransaction();
        em.merge(item);
        commitTransaction();
    }

    @Override
    public void beginTransaction () {
        em.getTransaction (). begin ();
    }
    @Override
    public void rollbackTransaction () {
        em.getTransaction (). rollback ();
    }
    @Override
    public void commitTransaction() {
        em.getTransaction (). commit ();
    }

    @Override
    public void deleteStockItem(StockItem stockItem) {
        beginTransaction();

        em.persist(stockItem);
        em.flush();
        em.remove(stockItem);

        commitTransaction();

        em.close();
        emf.close();
    }

    @Override
    public List<Purchase> findPurchases() {
        beginTransaction();
        Query query = em.createNativeQuery("SELECT * FROM Purchase", Purchase.class);
        List<Purchase> purchase = query.getResultList();
        commitTransaction();
        return purchase;
    }

    @Override
    public List<StockItem> findStockItems() {
        beginTransaction();
        Query query = em.createNativeQuery("SELECT * FROM StockItem", StockItem.class);
        List<StockItem> itemList = query.getResultList();
        commitTransaction();
        return  itemList;
    }

    @Override
    public StockItem findStockItem(long id) {
        return findStockItem(id);
    }
}