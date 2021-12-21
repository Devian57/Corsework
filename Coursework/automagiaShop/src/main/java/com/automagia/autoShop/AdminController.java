package com.automagia.autoShop;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AdminController {
    
    public ArrayList<User> listUsers = new ArrayList<>();
    
    public ObservableList<String> listUsersLogin = FXCollections
        .observableArrayList();
    
    private int user_role;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane buttonsBlock;

    @FXML
    private Label labelUserId;

    @FXML
    private Label labelUserName;

    @FXML
    private Label labelUserRole;

    @FXML
    private Label labelUserSalary;
    
    @FXML
    private Label labelHighestPost;
    
    @FXML
    private Label labelLowestPost;
    
    @FXML
    private Label labelSalaryText;
    
    @FXML
    private Label labelSalaryPlanted;
    
    @FXML
    private TextField inputSalaryField;

    @FXML
    private ComboBox<String> listUsersDropBlock;

    @FXML
    private Button logoutUserButton;

    @FXML
    private Button salaryDecreaseButton;

    @FXML
    private Button salaryIncreaseButton;

    @FXML
    private Pane userBlockInfo;
    
    @FXML
    private Pane salaryPlantBlock;

    @FXML
    private Button userDecreaseButton;

    @FXML
    private Button userDismissButton;

    @FXML
    private Label userLoginnedLabel;

    @FXML
    private Button userPromotionButton;

    @FXML
    void initialize() throws ClassNotFoundException, SQLException {
        userLoginnedLabel.setText("Welcome, " + SignInController.userLogin);
        labelHighestPost.setStyle("-fx-opacity: 0;");
        labelLowestPost.setStyle("-fx-opacity: 0;");
        getUsersFromDB();
        listUsersBlockInitialize();
        
        buttonsBlock.setStyle("-fx-opacity: 0;");
        
        salaryPlantBlock.setStyle("-fx-opacity: 0;");
    }

    private void listUsersBlockInitialize() {
        listUsersDropBlock.setValue("");
        listUsersDropBlock.setItems(listUsersLogin);
        listUsersDropBlock.setOnAction(e -> {
            labelUserId.setText(Integer.toString(listUsersDropBlock
                    .getSelectionModel().getSelectedIndex() + 1));
            labelUserName.setText(listUsersDropBlock.getValue());
            labelUserRole.setText(listUsers.get(listUsersDropBlock
                    .getSelectionModel().getSelectedIndex()).getRole());
            try {
                user_role = getRoleId(labelUserRole.getText());
            } catch (SQLException ex) {
                Logger.getLogger(AdminController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            labelUserSalary.setText(Integer.toString(listUsers
                    .get(listUsersDropBlock.getSelectionModel()
                            .getSelectedIndex()).getSalary()) + " руб");
            buttonsBlock.setStyle("-fx-opacity: 1;");
        });
    }
    
    @FXML
    private void checkUserLoginnedLabel() {}
    
    @FXML
    private void userPromotionAction() throws SQLException, 
            ClassNotFoundException {
        if (user_role == 5) {
            labelHighestPost.setStyle("-fx-opacity: 1;");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    labelHighestPost.setStyle("-fx-opacity: 0;");
                }
            }, Const.TIME_ANIMATION);
            return;
        }
        user_role++;
        System.out.println("role = " + user_role + " and login = " + labelUserName.getText());
        postUser();
    }
    
    @FXML
    private void userDecreaseAction() throws SQLException, 
            ClassNotFoundException {
        if (user_role == 3) {
            labelLowestPost.setStyle("-fx-opacity: 1;");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    labelLowestPost.setStyle("-fx-opacity: 0;");
                }
            }, Const.TIME_ANIMATION);
            return;
        }
        user_role--;
        System.out.println("role = " + user_role + " and login = " + labelUserName.getText());
        postUser();
    }
    
    private void postUser() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.promotionUser(labelUserName.getText(), user_role);
        
        ResultSet result = dbHandler.getRole(user_role);
        if (result.next()) {
            String name_role = result.getString("name_role");
            System.out.println(name_role);
            labelUserRole.setText(name_role);
            listUsers.get(Integer.parseInt(labelUserId.getText()) - 1)
                     .setRole(name_role);
        }
    }
    
    @FXML
    private void userDismissAction() throws ClassNotFoundException, 
            SQLException,
            IOException {
        String login_user = labelUserName.getText();
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.dismissUser(login_user);
        App.setRoot("adminPanel");
    }
    
    private boolean salaryAction() {
        if (inputSalaryField.getText().equals("")) {
            salaryPlantBlock.setStyle("-fx-opacity: 1;");
            labelSalaryText.setStyle("-fx-opacity: 1;");
            inputSalaryField.setStyle("-fx-opacity: 1;");
            return false;
        }
        return true;
    }
    
    @FXML
    protected void salaryIncreaseAction() throws SQLException, 
            ClassNotFoundException {
        int diffSalary = 0;
        if (!salaryAction()) return;
        try {
            diffSalary = Integer.parseInt(inputSalaryField.getText());
            inputSalaryField.setText("");
        } catch (NumberFormatException e) {}
        
        int currentSalary = Integer.parseInt(labelUserSalary.getText()
                .split(" ")[0]);
        String login_user = labelUserName.getText();
        int newSalary = currentSalary + diffSalary;
        setNewUserSalary(login_user, newSalary);
    }
    
    @FXML
    private void salaryDecreaseAction() throws SQLException, 
            ClassNotFoundException {
        int diffSalary = 0;
        if (!salaryAction()) return;
        try {
            diffSalary = Integer.parseInt(inputSalaryField.getText());
            inputSalaryField.setText("");
        } catch (NumberFormatException e) {}
        
        int currentSalary = Integer.parseInt(labelUserSalary.getText()
                .split(" ")[0]);
        String login_user = labelUserName.getText();
        int newSalary = currentSalary - diffSalary;
        if (newSalary <= 0)
            setNewUserSalary(login_user, currentSalary);
        else 
            setNewUserSalary(login_user, newSalary);
    }
    
    private void setNewUserSalary(String login_user, int newSalary) throws 
            SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultSalary = dbHandler
                .changeUserSalary(login_user, newSalary);
        
        if (resultSalary.next()) {
            int salary = resultSalary.getInt("salary_user");
            String salaryText = Integer.toString(salary) + " руб";
            labelUserSalary.setText(salaryText);
            listUsers.get(Integer.parseInt(labelUserId.getText()) - 1)
                    .seSalary(salary);
        }
        labelSalaryText.setStyle("-fx-opacity: 0;");
        inputSalaryField.setStyle("-fx-opacity: 0;");
        labelSalaryPlanted.setStyle("-fx-opacity: 1;");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                labelSalaryPlanted.setStyle("-fx-opacity: 0;");
                salaryPlantBlock.setStyle("-fx-opacity: 0;");
            }
        }, Const.TIME_ANIMATION);
    }
    
    @FXML
    void logoutUserFromDashboard(ActionEvent event) throws IOException {
        App.setRoot("signin");
    }

    void getUsersFromDB() throws ClassNotFoundException, 
            ClassNotFoundException, SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultUsers = dbHandler.getAllUsers();
        listUsers = new ArrayList<>();
        listUsersLogin = FXCollections.observableArrayList();
        for (; resultUsers.next();) {
            String login_user = resultUsers.getString("login_user");
            user_role = resultUsers.getInt("role_user");
            int salary_role = resultUsers.getInt("salary_user");
            String role_name = null;
            ResultSet resultRole = dbHandler.getRole(user_role);
            if (resultRole.next()) {
                role_name = resultRole.getString("name_role");
            }
            
            listUsersLogin.add(login_user);
            User user = new User(login_user, role_name, salary_role);
            listUsers.add(user);
        }
    }
    
    int getRoleId(String role) throws SQLException {
        int id_role = 0;
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultRoleId = dbHandler.getRoleId(role);
        
        if (resultRoleId.next()) {
            id_role = resultRoleId.getInt("id_role");
        }
        return id_role;
    }
    
}
