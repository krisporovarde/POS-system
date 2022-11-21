package ee.ut.math.tvt.salessystem.dao;
import ee.ut.math.tvt.salessystem.dataobjects.Order;
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
    public void saveOrder(Order order) {
        beginTransaction();
        em.persist(order);
        em.flush();
        commitTransaction();
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        beginTransaction();
        em.persist(stockItem);
        em.flush();
        commitTransaction();
    }

    @Override
    public void saveSoldItem(SoldItem item) {
        beginTransaction();
        em.persist(item);
        em.flush();
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
    public List<Order> findOrders() {
        return em.createQuery("SELECT Order FROM Order", Order.class).getResultList();
    }

    @Override
    public List<StockItem> findStockItems() {
        return em.createQuery("SELECT StockItem FROM StockItem", StockItem.class).getResultList();
    }

    @Override
    public StockItem findStockItem(long id) {
        String hql = "SELECT StockItem FROM StockItem E WHERE StockItem.id = ?1";
        Query query = em.createQuery(hql);
        query.setParameter(1, "%"+id+"%");
        List results = query.getResultList();
        return (StockItem) results;
    }
}