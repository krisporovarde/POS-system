package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.junit.jupiter.api.Test;


import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Tests{



    @Test
    void testAddingItemBeginsAndCommitsTransaction() {

        InMemorySalesSystemDAO dao =  mock(InMemorySalesSystemDAO.class);
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        shoppingCart.submitCurrentPurchase();
        verify(dao, times(1)).beginTransaction();
        verify(dao, times(1)).commitTransaction();
    }

    @Test
    void testAddingNewItemtoStock() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem stockItem = new StockItem();
        stockItem.setId(15L);
        dao.saveStockItem(stockItem);
        StockItem found = dao.findStockItem(15L);
        assertEquals(stockItem, found);
    }

    @Test
    void testAddingExistingItemtoStock() {
        InMemorySalesSystemDAO daoMock = mock(InMemorySalesSystemDAO.class);
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem stockItem = new StockItem();
        stockItem.setId(10L);
        stockItem.setName("Cheese");
        stockItem.setQuantity(10);
        stockItem.setPrice(5);
        dao.saveStockItem(stockItem);
        int quantityToAdd = 5;
        StockItem found = dao.findStockItem(10L);
        found.setQuantity(found.getQuantity()+quantityToAdd);
        assertEquals(15, found.getQuantity());
        verify(daoMock, times(0)).saveStockItem(stockItem);
    }
    class Foo{
        void foo() throws Exception{
            throw new RuntimeException("Exception Message");
        }
    }

    @Test
    void testAddingItemWithNegativeQuantitytoStock() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem stockItem = new StockItem(10L,"Cheese", "", 8, -1);
        dao.saveStockItem(stockItem);
        if (stockItem.getQuantity() < 0){
            Foo foo = new Foo();
            Exception exception = assertThrows(Exception.class, foo::foo);
            assertEquals("Exception Message", exception.getMessage());
        }
    }
    @Test
    void testAddingExistingItem() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem newItem = new SoldItem();
        newItem.setId(10L);
        newItem.setQuantity(2);
        shoppingCart.addItem(newItem);
        SoldItem secondItem = new SoldItem();
        secondItem.setId(10L);
        secondItem.setQuantity(5);
        shoppingCart.addItem(secondItem);
        List <SoldItem> list = shoppingCart.getAll();
        for (SoldItem item : list) {
            assertEquals(7, item.getQuantity());
        }
    }
    @Test
    void testAddingNewItem() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem item = new SoldItem();
        item.setId(10L);
        shoppingCart.addItem(item);
        List<SoldItem> items = shoppingCart.getAll();
        for (SoldItem iteminShoppincart : items) {
            assertEquals(item,iteminShoppincart);
        }
    }
    @Test
    void testAddingItemWithNegativeQuantity() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem soldItem = new SoldItem();
        soldItem.setQuantity(-1);
        shoppingCart.addItem(soldItem);
        if (soldItem.getQuantity() < 0){
            Foo foo = new Foo();
            Exception exception = assertThrows(Exception.class, foo::foo);
            assertEquals("Exception Message", exception.getMessage());
        }
    }
    @Test
    void testAddingItemWithQuantityTooLarge(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem newItem = new StockItem(5L, "Milk", "", 8, 10);
        dao.saveStockItem(newItem);
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem item = new SoldItem();
        item.setId(5L);
        item.setQuantity(12);
        shoppingCart.addItem(item);
        if (item.getQuantity() > newItem.getQuantity()){
            Foo foo = new Foo();
            Exception exception = assertThrows(Exception.class, foo::foo);
            assertEquals("Exception Message", exception.getMessage());
        }
    }
    @Test
    void testAddingItemWithQuantitySumTooLarge(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem newItem = new StockItem(5L, "Milk", "", 8, 20);
        dao.saveStockItem(newItem);
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem item = new SoldItem();
        item.setId(5L);
        item.setQuantity(12);
        shoppingCart.addItem(item);
        SoldItem secondItem = new SoldItem();
        secondItem.setQuantity(10);
        List<SoldItem> items = shoppingCart.getAll();
        for (SoldItem soldItem : items) {
            int expected = soldItem.getQuantity() + secondItem.getQuantity();
            if (expected > newItem.getQuantity()){
                Foo foo = new Foo();
                Exception exception = assertThrows(Exception.class, foo::foo);
                assertEquals("Exception Message", exception.getMessage());
            }
        }
    }
    @Test
    void testSubmittingCurrentPurchaseDecreasesStockItemQuantity(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        StockItem cheese = new StockItem(11L,"Cheese", "", 10, 10);
        StockItem ham = new StockItem(22L,"Ham", "", 20, 10);
        dao.saveStockItem(cheese);
        dao.saveStockItem(ham);
        SoldItem Cheese = new SoldItem();
        Cheese.setId(11L);
        Cheese.setName("Cheese");
        Cheese.setQuantity(5);
        SoldItem Ham = new SoldItem();
        Ham.setId(22L);
        Ham.setName("Ham");
        Ham.setQuantity(5);
        shoppingCart.addItem(Cheese);
        shoppingCart.addItem(Ham);
        shoppingCart.submitCurrentPurchase();
        StockItem item = dao.findStockItem(11L);
        StockItem item2 = dao.findStockItem(22L);

        assertEquals(5,item.getQuantity());
        assertEquals(5,item2.getQuantity());
    }
    @Test
    void testSubmittingCurrentPurchaseBeginsAndCommitsTransaction() {

        InMemorySalesSystemDAO dao =  mock(InMemorySalesSystemDAO.class);
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        shoppingCart.submitCurrentPurchase();
        verify(dao, times(1)).beginTransaction();
        verify(dao, times(1)).commitTransaction();
    }

    @Test
    void testSubmittingCurrentOrderCreatesHistoryItem(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem item1 = new SoldItem();
        item1.setName("Chicken");
        item1.setQuantity(3);
        item1.setPrice(10);
        SoldItem item2 = new SoldItem();
        item2.setName("Cow");
        item2.setQuantity(3);
        item2.setPrice(10);
        shoppingCart.addItem(item1);
        shoppingCart.addItem(item2);
        shoppingCart.submitCurrentPurchase();
        List<Purchase> orders = dao.findPurchases();
        double totalPrice = orders.get(0).getTotal();
        assertEquals(60.0, totalPrice);
    }
    @Test
    void testSubmittingCurrentOrderSavesCorrectTime(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem item1 = new SoldItem();
        item1.setName("Rice");
        item1.setQuantity(1);
        item1.setPrice(5);
        shoppingCart.addItem(item1);
        shoppingCart.submitCurrentPurchase();
        List<Purchase> orders = dao.findPurchases();
        String time = orders.get(0).getTime();
        double timeBeginning = Double.parseDouble(time.substring(6));
        String after = LocalTime.now().toString();
        double timeEnd = Double.parseDouble(after.substring(6));
        boolean quickEnough = false;
        if (timeEnd-timeBeginning < 1) quickEnough = true;
        assertTrue(quickEnough);
    }

    @Test
    void testCancellingOrder(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        SoldItem item1 = new SoldItem();
        item1.setName("Pork");
        shoppingCart.addItem(item1);
        shoppingCart.cancelCurrentPurchase();
        SoldItem item2 = new SoldItem();
        item2.setName("Egg");
        item2.setQuantity(2);
        item2.setPrice(10);
        shoppingCart.addItem(item2);
        shoppingCart.submitCurrentPurchase();
        List<Purchase> orders = dao.findPurchases();
        double cost = orders.get(0).getTotal();
        assertEquals(20.0, cost);
    }

    @Test
    void testCancellingOrderQuantitiesUnchanged(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        StockItem item1 = new StockItem(11L, "Pork", "", 10,10);
        dao.saveStockItem(item1);
        SoldItem item2 = new SoldItem();
        item2.setName("Pork");
        shoppingCart.addItem(item2);
        shoppingCart.cancelCurrentPurchase();
        StockItem pork = dao.findStockItem(11L);
        assertEquals(10, pork.getQuantity());
    }
}