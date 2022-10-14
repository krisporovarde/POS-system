package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private final SalesSystemDAO dao;

    @FXML
    private TextField barCodeWarehouse;
    @FXML
    private TextField quantityWarehouse;
    @FXML
    private TextField nameWarehouse;
    @FXML
    private TextField priceWarehouse;

    @FXML
    private Button addItem;

    @FXML
    private Button deleteItem;
    @FXML
    private TableView<StockItem> warehouseTableView;

    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshStockItems();
        // TODO refresh view after adding new items
    }

    @FXML
    public void refreshButtonClicked() {
        refreshStockItems();
    }

    private void refreshStockItems() {
        warehouseTableView.setItems(FXCollections.observableList(dao.findStockItems()));
        warehouseTableView.refresh();
    }

    public void addItemToWarehouse() {
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            int quantity;
            try {
                quantity = stockItem.getQuantity() + Integer.parseInt(quantityWarehouse.getText());
                stockItem.setQuantity(quantity);
            } catch (NumberFormatException e) {
                quantity = 1;
            }
            warehouseTableView.refresh();
        } else {
            long id = Long.parseLong(barCodeWarehouse.getText());
            String name = nameWarehouse.getText();
            String desc = "";
            double price = Double.parseDouble(priceWarehouse.getText());
            int quantity = Integer.parseInt(quantityWarehouse.getText());
            StockItem newStockItem = new StockItem(id, name, desc, price, quantity);
            dao.saveStockItem(newStockItem);
            warehouseTableView.refresh();
        }
    }

    public void deleteItemFromWarehouse() {
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            dao.deleteStockItem(stockItem);
            warehouseTableView.refresh();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.showAndWait();
        }
    }

    private StockItem getStockItemByBarcode() {
        try {
            long code = Long.parseLong(barCodeWarehouse.getText());
            return dao.findStockItem(code);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
