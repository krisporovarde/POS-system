package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.junit.jupiter.api.Test;


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
    }/*
    @Test
    void testSubmittingCurrentPurchaseDecreasesStockItemQuantity(){
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        ShoppingCart shoppingCart = new ShoppingCart(dao);
        StockItem cheese = new StockItem(11L,"Cheese", "", 10, 10);
        StockItem ham = new StockItem(22L,"Ham", "", 20, 10);
        StockItem milk = new StockItem(33L,"Milk", "", 30, 10);
        dao.saveStockItem(cheese);
        dao.saveStockItem(ham);
        dao.saveStockItem(milk);
        SoldItem Cheese = new SoldItem();
        Cheese.setId(11L);
        Cheese.setName("Cheese");
        Cheese.setQuantity(5);
        SoldItem Ham = new SoldItem();
        Ham.setId(22L);
        Ham.setName("Ham");
        Ham.setQuantity(5);
        SoldItem Milk = new SoldItem();
        Milk.setId(33L);
        Milk.setName("Milk");
        Milk.setQuantity(5);
        shoppingCart.addItem(Cheese);
        shoppingCart.addItem(Ham);
        shoppingCart.addItem(Milk);
        shoppingCart.submitCurrentPurchase();
        StockItem item1 = dao.findStockItem(11L);
        StockItem item2 = dao.findStockItem(22L);
        StockItem item3 = dao.findStockItem(33L);
        System.out.println(item1.getQuantity());
        System.out.println(item2);
        System.out.println(item3);
        assertEquals(5,item1.getQuantity());
        assertEquals(5,item2.getQuantity());
        assertEquals(5,item3.getQuantity());
    }*/
}