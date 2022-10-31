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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private static final Logger log = LogManager.getLogger(StockController.class);

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
        log.info("Stock items refreshed");
    }

    public void addItemToWarehouse() {
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null && inputCheck()) {
            int quantity;
            try {
                quantity = stockItem.getQuantity() + Integer.parseInt(quantityWarehouse.getText());
                stockItem.setQuantity(quantity);
            } catch (NumberFormatException e) {
                log.error(e);
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Quantity must be a positive number");
                errorAlert.showAndWait();
            }

        } else {
            if (inputCheck()){
                long id = Long.parseLong(barCodeWarehouse.getText());
                String name = nameWarehouse.getText();
                String desc = "";
                double price = Double.parseDouble(priceWarehouse.getText());
                int quantity = Integer.parseInt(quantityWarehouse.getText());
                log.debug("New stock item id: " +  id + " name: " + name + " price: " + price+ " quantity: " + quantity);
                StockItem newStockItem = new StockItem(id, name, desc, price, quantity);
                dao.saveStockItem(newStockItem);
                log.info("New item saved to stock");
            }
        }
        warehouseTableView.refresh();
        barCodeWarehouse.setText("");
        nameWarehouse.setText("");
        priceWarehouse.setText("");
        quantityWarehouse.setText("");
    }

    public boolean inputCheck(){
        try {
            if (barCodeWarehouse.getText().isEmpty() || nameWarehouse.getText().isEmpty() ||
                    priceWarehouse.getText().isEmpty() || quantityWarehouse.getText().isEmpty()){
                log.error("Input field empty");
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Fill in all the fields");
                errorAlert.showAndWait();
                return false;
            } else if (Integer.parseInt(barCodeWarehouse.getText()) < 0 || Integer.parseInt(quantityWarehouse.getText()) < 0 || Integer.parseInt(priceWarehouse.getText()) < 0){
                log.error("Negative input");
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input can't be negative");
                errorAlert.showAndWait();
                return false;
            } else  {
                String name = nameWarehouse.getText();
                char[] chars = name.toCharArray();
                for (char c : chars) {
                    if (Character.isDigit(c)){
                        log.error("Name can't contain numbers");
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("Name can't contain numbers");
                        errorAlert.showAndWait();
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e){
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Invalid input");
            errorAlert.showAndWait();
            return false;
        }
    }

    public void deleteItemFromWarehouse() {
        StockItem stockItem = getStockItemByBarcode();
        if (stockItem != null) {
            dao.deleteStockItem(stockItem);
            warehouseTableView.refresh();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Insert barcode to delete");
            errorAlert.showAndWait();
        }
        barCodeWarehouse.setText("");
        nameWarehouse.setText("");
        priceWarehouse.setText("");
        quantityWarehouse.setText("");
    }

    private StockItem getStockItemByBarcode() {
        try {
            long code = Long.parseLong(barCodeWarehouse.getText());
            return dao.findStockItem(code);
        } catch (NumberFormatException e) {
            log.error(e);
            return null;
        }
    }
}
