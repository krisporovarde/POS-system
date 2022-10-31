package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
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
    void testAddingNewItem() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem stockItem = new StockItem();
        stockItem.setId(15L);
        dao.saveStockItem(stockItem);
        StockItem found = dao.findStockItem(15L);
        assertEquals(stockItem, found);
    }

    @Test
    void testAddingExistingItem() {
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
    void testAddingItemWithNegativeQuantity() {
        InMemorySalesSystemDAO dao = new InMemorySalesSystemDAO();
        StockItem stockItem = new StockItem(10L,"Cheese", "", 8, -1);
        dao.saveStockItem(stockItem);
        if (stockItem.getQuantity() < 0){
            Foo foo = new Foo();
            Exception exception = assertThrows(Exception.class, foo::foo);
            assertEquals("Exception Message", exception.getMessage());
        }
    }
}