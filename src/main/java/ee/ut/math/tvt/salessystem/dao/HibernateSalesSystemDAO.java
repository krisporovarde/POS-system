package ee.ut.math.tvt.salessystem.dao;
import ee.ut.math.tvt.salessystem.dataobjects.Order;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
    public List<StockItem> findStockItems() {
        return null;
    }

    @Override
    public StockItem findStockItem(long id) {
        return null;
    }

    @Override
    public void saveStockItem(StockItem stockItem) {

    }

    @Override
    public void saveSoldItem(SoldItem item) {

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
    public void commitTransaction () {
        em.getTransaction (). commit ();
    }

    @Override
    public void deleteStockItem(StockItem stockItem) {

    }

    @Override
    public void saveOrder() {

    }

    @Override
    public List<Order> findOrders() {
        return null;
    }
}