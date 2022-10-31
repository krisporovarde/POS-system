package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.Order;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private TableView<Order> orderTableView;
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
            if (newSelection != null) {
                Order order = orderTableView.getSelectionModel().getSelectedItem();
                soldItemTableView.setItems(FXCollections.observableList(order.getItems()));
                soldItemTableView.refresh();
                System.out.println(order);
            }
        });
    }

    private void showAllOrders(){
        orderTableView.setItems(FXCollections.observableList(dao.findOrders()));
        orderTableView.refresh();
    }
    @FXML
    public void showAllOrdersButtonClicked() {
        showAllOrders();
    }

    public void showLast10ButtonClicked(){
        List<Order> orders = dao.findOrders();
        if (orders.size() <= 10){
            orderTableView.setItems(FXCollections.observableList(orders));
        }
        else {
            List<Order> last10orders = new ArrayList<Order>();
            for (int i = orders.size()-1; i > orders.size()-11; i--) {
                last10orders.add(orders.get(i));
            }
            orderTableView.setItems(FXCollections.observableList(last10orders));
        }
    }

    public void showBetweenDatesButtonClicked(){
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/dd/MM");
//        LocalDate startFormatted = LocalDate.parse(start, format);
//        String endFormatted = date.format(end);
        List<Order> betweenDatesOrders = new ArrayList<Order>();
        for (Order order : dao.findOrders()) {
            String date = order.getDate();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/dd/MM");
            LocalDate dateFormatted = LocalDate.parse(date, format);
            if (start.isBefore(dateFormatted) && end.isAfter(dateFormatted)){
                betweenDatesOrders.add(order);
            }
        }
        orderTableView.setItems(FXCollections.observableList(betweenDatesOrders));
    }
}
