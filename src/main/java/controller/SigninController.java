package controller;

import data.AccountDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Account;
import java.net.URL;
import java.util.ResourceBundle;

public class SigninController implements Initializable {

    @FXML private Label labelMessage;
    @FXML private AnchorPane parent;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Safe CSS loading
            parent.getStylesheets().add(getClass().getResource("/style/Sign-in.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS Error ignored.");
        }
    }

    @FXML
    void login(ActionEvent event) {
        if (txtUsername.getText().isBlank() || txtPassword.getText().isBlank()) {
            labelMessage.setText("All fields are required");
            return;
        }

        try {
            AccountDao accountDao = new AccountDao();
            Account user = accountDao.read(txtUsername.getText().trim());

            if (user == null) {
                labelMessage.setText("This User doesn't exist.");
            } else {
                if (user.getPassword().equals(txtPassword.getText().trim())) {
                    Account.setCurrentUser(user);

                    // 1. Close current window
                    Stage currentStage = (Stage) parent.getScene().getWindow();
                    currentStage.close();

                    // 2. Open Home Page using your Helper Class
                    System.out.println("Login Success! Calling ApplicationController...");
                    ApplicationController.appLoad(new Stage()); 

                } else {
                    labelMessage.setText("Wrong Password");
                }
            }
        } catch (Exception e) {
            labelMessage.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Keep your other methods (SwitchtoSignup) as they were...
    @FXML
    void SwitchtoSignup(ActionEvent event) {
        // ... (Keep your existing code here)
    }
}