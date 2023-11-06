package com.example.javafxlogin;

import dao.UsersDAO;
import data.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;
import org.mindrot.jbcrypt.*;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private Hyperlink createAccountBtn;
    @FXML
    private Hyperlink loginAccountBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    @FXML
    private BorderPane login_Form;

    @FXML
    private PasswordField passwordTextField;



    @FXML
    private PasswordField passwordTextField1;

    @FXML
    private Button registerButton;

    @FXML
    private BorderPane register_Form;


    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField usernameTextField1;

    public User user;

    @FXML
    public void loginAccount(ActionEvent event) throws SQLException, IOException {
        Alert alert;
        if (usernameTextField1.getText().isEmpty() && passwordTextField1.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Pls enter username & password");
            alert.showAndWait();
        } else {
            String username = usernameTextField1.getText();
            String enterPassword = passwordTextField1.getText();

            User user = UsersDAO.getInstance().checkUser(username);
            boolean checkpw = BCrypt.checkpw(enterPassword, user.getPassword());

            if (checkpw) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Login");
                alert.showAndWait();

                //close login form
                login_Form.getScene().getWindow().hide();

                // open crud form
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("crudForm.fxml"));
                Parent root = fxmlLoader.load();

                CRUDController crudController = fxmlLoader.getController();
                crudController.initData(user);

                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Login failed");
                alert.showAndWait();
            }

        }
    }

    @FXML
    void registerAccount(ActionEvent event) throws SQLException {
        Alert alert;
        String username = null;
        String password = null;

        if (usernameTextField.getText().isEmpty() && passwordTextField.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Pls enter username & password");
            alert.showAndWait();
        } else if (passwordTextField.getText().length() < 5 && usernameTextField.getText().length() < 5) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("username & password must have at least 5 character");
            alert.showAndWait();
        } else {
            username = usernameTextField.getText();
            password = passwordTextField.getText();

            String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // check if username already exists
            String checkData = "select * from users where username = ? ";

            PreparedStatement preparedStatement = ConnectDB.getConnection().prepareStatement(checkData);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Username: '" + username + "' already exists ");
                alert.showAndWait();

                preparedStatement.close();
                ConnectDB.getConnection().close();

            } else {
                if (UsersDAO.getInstance().save(new User(username, hashPassword,emailTextField.getText()))) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully register");
                    alert.showAndWait();

                    login_Form.setVisible(true);
                    register_Form.setVisible(false);

                    usernameTextField.setText("");
                    passwordTextField.setText("");
                    emailTextField.setText("");


                }
            }
        }
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == createAccountBtn) {
            login_Form.setVisible(false);
            register_Form.setVisible(true);
        } else if (event.getSource() == loginAccountBtn) {
            register_Form.setVisible(false);
            login_Form.setVisible(true);

        }
    }



}





