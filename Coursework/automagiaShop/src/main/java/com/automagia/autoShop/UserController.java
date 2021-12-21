package com.automagia.autoShop;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class UserController {
    
    public ArrayList<Order> listOrders = new ArrayList<>();
    
    public ObservableList<String> listOrdersTitle = FXCollections
        .observableArrayList();
    
    public ArrayList<Order> listFreeOrders = new ArrayList<>();
    
    public ObservableList<String> listFreeOrdersTitle = FXCollections
        .observableArrayList();
    
    private ResultSet resultUserOrder;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button acceptOrderButton;

    @FXML
    private Pane buttonsBlock;

    @FXML
    private Label labelFreeUser;

    @FXML
    private Label labelOrderDescription;

    @FXML
    private Label labelOrderId;

    @FXML
    private Label labelOrderStatus;

    @FXML
    private Label labelOrderTitle;

    @FXML
    private ComboBox<String> listFreeOrdersDropBlock;

    @FXML
    private Button logoutUserButton;

    @FXML
    private Button rejectOrderButton;
    
    @FXML
    private Button completeOrderButton;

    @FXML
    private Label userLoginnedLabel;

    @FXML
    void logoutUserFromDashboard(ActionEvent event) throws IOException {
        App.setRoot("signin");
    }
    
    @FXML
    void acceptOrderAction(ActionEvent event) throws ClassNotFoundException, 
            SQLException,
            IOException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int id_order = listFreeOrders.get(Integer.parseInt(labelOrderId
                .getText()) - 1).getId();
        String login_user = SignInController.userLogin;
        dbHandler.changeOrderStatus(login_user, id_order, "occupied");
        App.setRoot("userDashboard");
    }

    @FXML
    void rejectOrderAction(ActionEvent event) throws ClassNotFoundException, 
            SQLException,
            IOException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int id_order = listOrders.get(Integer.parseInt(labelOrderId
                .getText()) - 1).getId();
        String login_user = SignInController.userLogin;
        dbHandler.changeOrderStatus(null, id_order, "free");
        App.setRoot("userDashboard");
    }
    
    @FXML
    void completeOrderAction() throws ClassNotFoundException, 
            SQLException,
            IOException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int id_order = listOrders.get(Integer.parseInt(labelOrderId
                .getText()) - 1).getId();
        String login_user = SignInController.userLogin;
        dbHandler.changeOrderStatus(login_user, id_order, "completed");
        App.setRoot("userDashboard");
    }
    
    public boolean checkUserFree(String login_user) throws SQLException, 
            ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        resultUserOrder = dbHandler.checkUserOrder(login_user);
        return resultUserOrder.next();
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        String login_user = SignInController.userLogin;
        userLoginnedLabel.setText("Welcome, " + login_user);
        getOrdersFromDatabase();
        if (checkUserFree(login_user)) {
            labelFreeUser.setStyle("-fx-opacity: 0;");
            int id = 0;
            int id_order = resultUserOrder.getInt("id_order");
            for (int i = 0; i < listOrders.size(); i++) {
                if (listOrders.get(i).getId() == id_order)
                    id = i;
            }
            id++;
            String title = resultUserOrder.getString("title_order");
            String description = resultUserOrder.getString("description_order");
            String status = resultUserOrder.getString("status_order");
            labelOrderId.setText(Integer.toString(id));
            labelOrderTitle.setText(title);
            labelOrderDescription.setText(description);
            labelOrderStatus.setText(status);
            acceptOrderButton.setDisable(true);
        } else {
            buttonsBlock.setStyle("-fx-opacity: 0;");
            getFreeOrdersFromDatabase();
            listFreeOrdersDropBlock.setValue("");
            listFreeOrdersDropBlock.setItems(listFreeOrdersTitle);
            listFreeOrdersDropBlock.setOnAction(e -> {
            labelOrderId.setText(Integer.toString(listFreeOrdersDropBlock
                    .getSelectionModel().getSelectedIndex() + 1));
            labelOrderTitle.setText(listFreeOrdersDropBlock.getValue());
            labelOrderDescription.setText(listFreeOrders.get(Integer
                    .parseInt(labelOrderId.getText()) - 1).getDescription());
            labelOrderStatus.setText(listFreeOrders.get(Integer
                    .parseInt(labelOrderId.getText()) - 1).getStatus());
            labelFreeUser.setStyle("-fx-opacity: 0;");
            buttonsBlock.setStyle("-fx-opacity: 1;");
            rejectOrderButton.setDisable(true);
            completeOrderButton.setDisable(true);
            });
        }
    }
    
    private void getFreeOrdersFromDatabase() throws SQLException, 
            ClassNotFoundException {
        listFreeOrders = new ArrayList<>();
        listFreeOrdersTitle = FXCollections.observableArrayList();
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultOrders = dbHandler.getFreeOrders();
        for (; resultOrders.next();) {
            int id_order = resultOrders.getInt("id_order");
            String title = resultOrders.getString("title_order");
            String description = resultOrders.getString("description_order");
            String login_user = resultOrders.getString("user_order");
            String status = resultOrders.getString("status_order");
            listFreeOrdersTitle.add(title);
            Order order = new Order(id_order, title, description, login_user, 
                    status);
            listFreeOrders.add(order);
        }
    }
    
    private void getOrdersFromDatabase() throws SQLException, 
            ClassNotFoundException {
        listOrders = new ArrayList<>();
        listOrdersTitle = FXCollections.observableArrayList();
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultOrders = dbHandler.getAllOrders();
        for (; resultOrders.next();) {
            int id_order = resultOrders.getInt("id_order");
            String title = resultOrders.getString("title_order");
            String description = resultOrders.getString("description_order");
            String login_user = resultOrders.getString("user_order");
            String status = resultOrders.getString("status_order");
            listOrdersTitle.add(title);
            Order order = new Order(id_order, title, description, login_user, 
                    status);
            listOrders.add(order);
        }
    }

}
