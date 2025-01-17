package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final List<SoldItem> soldItemList;
    private final List<Purchase> purhcaseList;


    public InMemorySalesSystemDAO() {
        List<StockItem> items = new ArrayList<StockItem>();
        items.add(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        items.add(new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8));
        items.add(new StockItem(3L, "Frankfurters", "Beer sauseges", 15.0, 12));
        items.add(new StockItem(4L, "Free Beer", "Student's delight", 0.0, 100));
//        List<Order> orders = new ArrayList<Order>();
//        List<SoldItem> testSoldItemList = new ArrayList<SoldItem>();
//        StockItem testStockItem = new StockItem(1L, "Lays chips", "Potato chips", 11.0, 1);
//        testSoldItemList.add(new SoldItem(testStockItem, 1));
//        orders.add(new Order(1L,"2022/31/10", "18:31:57", 11, testSoldItemList));
        this.stockItemList = items;
        this.soldItemList = new ArrayList<SoldItem>();
        this.purhcaseList = new ArrayList<Purchase>();
    }

    @Override
    public void saveSoldItem(SoldItem item) {
        soldItemList.add(item);
    }

    public void savePurchase(Purchase purchase){
        purhcaseList.add(purchase);
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    public void deleteStockItem(StockItem stockItem){
        int index = stockItemList.indexOf(stockItem);
        stockItemList.remove(index);
    }
    @Override
    public void beginTransaction() {
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void commitTransaction() {
    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
    }

    @Override
    public StockItem findStockItem(long id) {
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }

    public List<Purchase> findPurchases(){
        return purhcaseList;
    }
}
