package com.automagia.autoShop;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Label successfullLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private Button authSignInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_confirm_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    void initialize() {
        
    }
    
    @FXML
    public void authLoginController() {
        login_field.setText("");
        login_field.setPromptText("Wrong username!");
    }
    
    @FXML
    public void authPassController() {
        pass_field.setText("");
        pass_field.setPromptText("Wrong pass!");
    }
    
    @FXML
    public void authPassConfirmController() {
        pass_confirm_field.setText("");
        pass_confirm_field.setPromptText("Passwords don't match!");
    }
    
    @FXML
    private void authSignUpUser() throws IOException, SQLException, 
            ClassNotFoundException {
        String username = login_field.getText();
        String password = pass_field.getText();
        String passConf = pass_confirm_field.getText();
        if (checkUserData(username, password, passConf)) {
            authRegisterUser(username, password);
        } else {
            errorLabel.setStyle("-fx-opacity: 1");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    errorLabel.setStyle("-fx-opacity: 0");
                }
            }, Const.TIME_ANIMATION);
        }
    }

    @FXML
    private void switchToLoginPage() throws IOException {
        App.setRoot("signin");
    }

    private void authRegisterUser(String username, String password) {
            System.out.println("User " + username 
                    + " has been registered, password - " + password);
            successfullLabel.setStyle("-fx-opacity: 1");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    successfullLabel.setStyle("-fx-opacity: 0");
                }
            }, Const.TIME_ANIMATION);
    }
    
    public boolean checkUserData(String username, String password, String passConf)
            throws IOException, ClassNotFoundException, SQLException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getLoginUser(username);
        if ((!username.equals("") && !username.contains(" ")) 
                && (!password.equals("") && !password.contains(" "))
                    && passConf.equals(password) && !result.next()) {
            User user = new User(username, password);
            dbHandler.signUpUser(user);
            return true;
        } else {
            if (username.equals("") || username.contains(" ")) {
                authLoginController();
            } else if (password.equals("") || password.contains(" ")) {
                authPassController();
            } else if (!passConf.equals(password) || passConf.contains(" ")) {
                authPassConfirmController();
            }
        }
        return false;
    }
}
