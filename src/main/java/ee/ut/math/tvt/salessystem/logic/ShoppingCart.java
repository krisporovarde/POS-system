package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class ShoppingCart {



    private final SalesSystemDAO dao;
    private final List<SoldItem> items = new ArrayList<>();

    public ShoppingCart(SalesSystemDAO dao) {
        this.dao = dao;
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(SoldItem item) {
        // TODO In case such stockItem already exists increase the quantity of the existing stock
        // TODO verify that warehouse items' quantity remains at least zero or throw an exception
        boolean isItemAdded = false;
        if (items.size() ==0){
            items.add(item);
        } else {
            for (SoldItem soldItem : items) {
                if (Objects.equals(soldItem.getName(), item.getName())){
                    soldItem.setQuantity(soldItem.getQuantity()+item.getQuantity());
                    soldItem.setSum(soldItem.getSum() + item.getSum());
                    isItemAdded = true;
                    break;
                }
            }
            if (!isItemAdded){
                items.add(item);
            }
        }
        //log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    public List<SoldItem> getAll() {
        return items;
    }

    public void cancelCurrentPurchase() {
        items.clear();
    }

    public void submitCurrentPurchase() {
        // TODO decrease quantities of the warehouse stock

        // note the use of transactions. InMemorySalesSystemDAO ignores transactions
        // but when you start using hibernate in lab5, then it will become relevant.
        // what is a transaction? https://stackoverflow.com/q/974596
        dao.beginTransaction();
        try {
            for (SoldItem item : items) {
                dao.saveSoldItem(item);
            }

            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/dd/MM");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String dateFormatted = date.format(now);
            String timeFormatted = time.format(now);
            double soldItemsTotal = calculateSoldItemsTotal();
            Set<SoldItem> copyOfSoldItems = new HashSet<SoldItem>(items);
            Purchase purchase = new Purchase(1L ,dateFormatted, timeFormatted, soldItemsTotal, copyOfSoldItems);

            dao.savePurchase(purchase);
            dao.commitTransaction();
            items.clear();
        } catch (Exception e) {
            dao.rollbackTransaction();
            throw e;
        }
    }

    public double calculateSoldItemsTotal(){
        double total = 0;
        for (SoldItem soldItem : items) {
            total += soldItem.getSum();
        }
        return total;
    }
}
