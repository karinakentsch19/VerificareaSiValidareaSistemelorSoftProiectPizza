package pizzashop.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.Calendar;

public class KitchenGUIController {
    @FXML
    private ListView<String> kitchenOrdersList;
    @FXML
    public Button cook;
    @FXML
    public Button ready;

    protected static ObservableList<String> order = FXCollections.observableArrayList();
    private Object selectedOrder;
    private final Calendar now = Calendar.getInstance();

    //thread for adding data to kitchenOrderList
    public Thread addOrders = new Thread(() -> {
        while (true) {
            Platform.runLater(() -> kitchenOrdersList.setItems(order));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                break;
            }

        }
    });


    private void onCookAction() {
        selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
        kitchenOrdersList.getItems().remove(selectedOrder);
        kitchenOrdersList.getItems().add(selectedOrder.toString()
                .concat(" Cooking started at: ").toUpperCase()
                .concat(now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE)));
    }

    private void onReadyAction() {
        selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
        kitchenOrdersList.getItems().remove(selectedOrder);
        String extractedTableNumberString = selectedOrder.toString().subSequence(5, 6).toString();
        int extractedTableNumberInteger = Integer.parseInt(extractedTableNumberString);
        System.out.println("--------------------------");
        System.out.println("Table " + extractedTableNumberInteger + " ready at: " + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE));
        System.out.println("--------------------------");
    }

    public void initialize() {
        //starting thread for adding data to kitchenOrderList
        addOrders.setDaemon(true);
        addOrders.start();
        //Controller for Cook Button
        cook.setOnAction(event ->
                onCookAction()
        );
        //Controller for Ready Button
        ready.setOnAction(event ->
                onReadyAction());
    }
}