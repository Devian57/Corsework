package com.automagia.autoShop;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ManagerController {
    
    public ArrayList<Order> listOrders = new ArrayList<>();
    
    public ObservableList<String> listOrdersTitle = FXCollections
        .observableArrayList();

    @FXML
    private Pane buttonsBlock;

    @FXML
    private TextArea inputOrderDescription;

    @FXML
    private TextField inputOrderTitle;

    @FXML
    private TextField inputOrderUser;

    @FXML
    private Label labelOrderDescription;

    @FXML
    private Label labelOrderId;

    @FXML
    private Label labelOrderSuccessAction;

    @FXML
    private Label labelOrderTitle;

    @FXML
    private Label labelUserOrder;
    
    @FXML
    private Label labelOrderStatus;
    
    @FXML
    private Label labelUserNotFound;

    @FXML
    private Button logoutUserButton;

    @FXML
    private Button orderDropButton;

    @FXML
    private Button orderInsertButton;

    @FXML
    private Label userLoginnedLabel;
    
    @FXML
    private ComboBox<String> listOrdersDropBlock;
    
    @FXML
    void orderDropAction(ActionEvent event) throws ClassNotFoundException, 
            SQLException, IOException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        int id_order = listOrders.get(Integer.parseInt(labelOrderId
                .getText()) - 1).getId();
        dbHandler.dropOrder(id_order);
        labelOrderSuccessAction.setStyle("-fx-opacity: 1;");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                labelOrderSuccessAction.setStyle("-fx-opacity: 0;");
                try {
                    App.setRoot("managerPanel");
                } catch (IOException ex) {
                    Logger.getLogger(ManagerController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
         }, Const.TIME_ANIMATION);
    }

    @FXML
    void orderInsertAction(ActionEvent event) throws ClassNotFoundException, 
            SQLException {
        String title = inputOrderTitle.getText();
        String description = inputOrderDescription.getText();
        String user = inputOrderUser.getText();
        if (title.equals("") || description.equals("")) return;
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultUser = dbHandler.getLoginUser(user);
        if (!user.equals("")) { 
            if (!resultUser.next()) {
            labelUserNotFound.setStyle("-fx-opacity: 1;");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
            @Override
            public void run() {
                labelUserNotFound.setStyle("-fx-opacity: 0;");
            }
            }, Const.TIME_ANIMATION);
            return;
        }
            dbHandler.createOrder(title, description, user);
        } else
            dbHandler.createOrder(title, description);
        inputOrderTitle.setText("");
        inputOrderDescription.setText("");
        inputOrderUser.setText("");
        labelOrderSuccessAction.setStyle("-fx-opacity: 1;");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                labelOrderSuccessAction.setStyle("-fx-opacity: 0;");
                try {
                    App.setRoot("managerPanel");
                } catch (IOException ex) {
                    Logger.getLogger(ManagerController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
         }, Const.TIME_ANIMATION);
            
        
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        userLoginnedLabel.setText("Welcome, " + SignInController.userLogin);
        labelOrderSuccessAction.setStyle("-fx-opacity: 0;");
        labelUserNotFound.setStyle("-fx-opacity: 0;");
        getOrdersFromDatabase();
        
        listOrdersDropBlock.setValue("");
        listOrdersDropBlock.setItems(listOrdersTitle);
        listOrdersDropBlock.setOnAction(e -> {
            labelOrderId.setText(Integer.toString(listOrdersDropBlock
                    .getSelectionModel().getSelectedIndex() + 1));
            labelOrderTitle.setText(listOrdersDropBlock.getValue());
            labelOrderDescription.setText(listOrders.get(Integer
                    .parseInt(labelOrderId.getText()) - 1).getDescription());
            labelUserOrder.setText(listOrders.get(Integer
                    .parseInt(labelOrderId.getText()) - 1).getUserOrder());
            labelOrderStatus.setText(listOrders.get(Integer
                    .parseInt(labelOrderId.getText()) - 1).getStatus());
        });
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

    @FXML
    void logoutUserFromDashboard(ActionEvent event) throws IOException {
        App.setRoot("signin");
    }
}
