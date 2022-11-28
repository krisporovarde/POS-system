package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Purchase;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {

    private static final Logger log = LogManager.getLogger(HistoryController.class);
    private final SalesSystemDAO dao;
    @FXML
    private Button showBetweenDates;
    @FXML
    private Button showLast10;
    @FXML
    private Button showAll;
    @FXML
    private TableView<Purchase> orderTableView;
    @FXML
    private TableView<SoldItem> soldItemTableView;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: implement
        showAllOrders();
        orderTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            orderTableView.refresh();
            Purchase purchase = orderTableView.getSelectionModel().getSelectedItem();
            soldItemTableView.setItems(FXCollections.observableList(purchase.getItems()));
            soldItemTableView.refresh();
        });
    }

    private void showAllOrders(){
        orderTableView.setItems(FXCollections.observableList(dao.findPurchases()));
        orderTableView.refresh();
    }
    @FXML
    public void showAllOrdersButtonClicked() {
        showAllOrders();
    }

    public void showLast10ButtonClicked(){
        List<Purchase> orders = dao.findPurchases();
        if (orders.size() <= 10){
            orderTableView.setItems(FXCollections.observableList(orders));
        }
        else {
            List<Purchase> last10orders = new ArrayList<Purchase>();
            for (int i = orders.size()-1; i > orders.size()-11; i--) {
                last10orders.add(orders.get(i));
            }
            orderTableView.setItems(FXCollections.observableList(last10orders));
        }
    }

    public void showBetweenDatesButtonClicked(){
        try {
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();

            //        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/dd/MM");
//        LocalDate startFormatted = LocalDate.parse(start, format);
//        String endFormatted = date.format(end);
            List<Purchase> betweenDatesOrders = new ArrayList<Purchase>();

            if (start.isBefore(end)) {
                for (Purchase order : dao.findPurchases()) {
                    String date = order.getDate();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/dd/MM");
                    LocalDate dateFormatted = LocalDate.parse(date, format);
                    if (start.isBefore(dateFormatted) && end.isAfter(dateFormatted)) {
                        betweenDatesOrders.add(order);
                    }
                }
                orderTableView.setItems(FXCollections.observableList(betweenDatesOrders));
            }
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Start date must be before end date");
                errorAlert.showAndWait();
            }
        } catch (Exception e){
            log.error(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Select two dates");
            errorAlert.showAndWait();
        }

    }
}
