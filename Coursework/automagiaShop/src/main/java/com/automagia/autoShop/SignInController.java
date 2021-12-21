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

public class SignInController {
    
    public static String userLogin;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authSignInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_field;
    
    @FXML
    private Label warn;

    @FXML
    void initialize() throws IOException {
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
    private void authCheckUser() throws IOException, SQLException, 
            ClassNotFoundException {
        try {
            String username = login_field.getText();
            String password = pass_field.getText();
            if (checkUserData(username, password)) {
                authLogin(username, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
            authLoginController();
            authPassController();
        }
    }
    
    @FXML
    private void switchToRegisterPage() throws IOException {
        App.setRoot("signup");
    }

    private void authLogin(String loginUser, String loginPass) throws 
            SQLException, ClassNotFoundException, IOException {
        
        if (checkUserFromDatabase(loginUser, loginPass)) {
            System.out.println("User " + loginUser + 
                    " has been logined, password - " + loginPass);
            userLogin = loginUser;
            if (loginUser.equals("Admin")) 
                App.setRoot("adminPanel");
            else if (loginUser.equals("Director"))
                App.setRoot("managerPanel");
            else
                App.setRoot("userDashboard");
                
        } else {
            AnimationShake animationLogin = new AnimationShake(login_field);
            AnimationShake animationPassword = new AnimationShake(pass_field);
            animationLogin.play();
            animationPassword.play();
            warn.setStyle("-fx-opacity: 1");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    warn.setStyle("-fx-opacity: 0");
                }
            }, Const.TIME_ANIMATION);
        }
    }
    
    public boolean checkUserFromDatabase(String loginUser, String loginPass) 
            throws SQLException, ClassNotFoundException, IOException {        
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User(loginUser, loginPass);
        ResultSet result = dbHandler.getUser(user);
        
        return result.next();
    }
    
    public boolean checkUserData(String username, String password) 
            throws IOException {
        if ((!username.equals("") && !username.contains(" ")) 
                && (!password.equals("") && !password.contains(" "))) {
            return true;
        } else {
            if (username.equals("") || username.contains(" ")) {
                authLoginController();
            } else if (password.equals("") || password.contains(" ")) {
                authPassController();
            }
            return false;
        }
    }
}
