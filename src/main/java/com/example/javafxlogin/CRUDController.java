package com.example.javafxlogin;

import dao.CustomerDAO;
import dao.UsersDAO;
import data.ConnectDB;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.SimpleSelector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import model.Customer;
import model.User;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class CRUDController implements Initializable {

    @FXML
    private Button crud_addBtn;

    @FXML
    private Label crud_admin;

    @FXML
    private DatePicker crud_birthday;


    @FXML
    private TableColumn<Customer, String> crud_col_birthday;


    @FXML
    private TableColumn<Customer, String> crud_col_email;

    @FXML
    private TableColumn<Customer, String> crud_col_fullname;

    @FXML
    private TableColumn<Customer, String> crud_col_gender;

    @FXML
    private TableColumn<Customer, String> crud_col_id;

    @FXML
    private TableColumn<Customer, String> crud_col_index;

    @FXML
    private TableColumn<Customer, String> crud_col_status;

    @FXML
    private TableColumn<Customer, String> crud_col_username;

    @FXML
    private TableView<Customer> crud_tableView;

    @FXML
    private Button crud_deleteBtn;

    @FXML
    private TextField crud_email;

    @FXML
    private TextField crud_fullname;

    @FXML
    private ComboBox<String> crud_gender;

    @FXML
    private Button crud_updateBtn;
    @FXML
    private TextField crud_username;
    @FXML
    private TextField keywordFieldText;


    @FXML
    private Hyperlink crud_logoutBtn;
    @FXML
    private StackPane scenePane;

    Stage stage;
    FXMLLoader fxmlLoader;
    Scene scene;
    Alert alert;
    private ObservableList<Customer> customerShowData;


    public void customerAddBtn() throws SQLException {

        if (crud_fullname.getText().isEmpty() && crud_username.getText().isEmpty() && crud_email.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Pls fill all fields");
            alert.showAndWait();
        } else {

            String email = crud_email.getText();
            String checkData = "select * from customer where email = ? ";

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = ConnectDB.getConnection().prepareStatement(checkData);
                preparedStatement.setString(1, email);

                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Email: '" + email + "' already exists ");
                    alert.showAndWait();
                } else {
                    Date date = Date.valueOf(LocalDate.parse(crud_birthday.getValue().toString()));

                    int num = CustomerDAO.getInstance().customerList().size();
                    String id_cus = "cus0" + num;
                    String fullname = crud_fullname.getText();
                    String username = crud_username.getText();
                    String gender = crud_gender.getValue();
                    Date birthday = date;
                    Customer customer = new Customer(id_cus, fullname, username, email, gender, date);
                    CustomerDAO.getInstance().save(customer);
                    customerShowData.add(customer);

                    // update new data to table view
                    customerShowData();
                    customerClearBtn();

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (preparedStatement != null) preparedStatement.close();
                if (ConnectDB.getConnection() != null) ConnectDB.getConnection().close();

            }
        }
    }

    public void customerClearBtn() {
        crud_fullname.setText("");
        crud_username.setText("");
        crud_email.setText("");
        crud_birthday.setValue(null);
        crud_gender.setValue(null);

    }

    public void customerDeleteBtn() throws SQLException {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setContentText("Are you sure you want to delete? ");
        if (alert.showAndWait().get() == ButtonType.OK) {

            int index = crud_tableView.getSelectionModel().getSelectedIndex();
            Customer customer = crud_tableView.getSelectionModel().getSelectedItem();

            // update in DB
            String id_cus = customer.getId_customer();
            CustomerDAO.getInstance().updateStatus(id_cus, false);
        }
        customerShowData();
        customerClearBtn();

    }

    public void customerUpdateBtn() throws SQLException {
        if (crud_fullname.getText().isEmpty() || crud_username.getText().isEmpty() || crud_email.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("pls fill all fields");
            alert.showAndWait();
        } else {
            Customer customer = crud_tableView.getSelectionModel().getSelectedItem();
            String id_cus = customer.getId_customer();
            Date date = Date.valueOf(LocalDate.parse(crud_birthday.getValue().toString()));

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(" Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to update? ");
            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean result;
                result = CustomerDAO.getInstance().update(new Customer(id_cus, crud_fullname.getText()
                        , crud_username.getText()
                        , crud_email.getText()
                        , crud_gender.getValue()
                        , date
                        , customer.isStatus()));


                if (result) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated");
                    alert.showAndWait();
                }

                customerShowData();
                customerClearBtn();
            }

        }
    }

    private String[] genderList = {"Male", "Female", "Others"};

    public void customerGender() {
        ObservableList list = FXCollections.observableArrayList(genderList);
        crud_gender.setItems(list);
    }


    public void customerShowData() throws SQLException {

        customerShowData = CustomerDAO.getInstance().customerList();

        crud_col_index.setCellValueFactory(p -> {
            String index = p.getTableView().getItems().indexOf(p.getValue()) + 1 + "";
            return new SimpleStringProperty(index);
        });

//        crud_col_id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getId_customer()));
        crud_col_id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getId_customer()));
        crud_col_fullname.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFullname()));
        crud_col_username.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getUsername()));
        crud_col_email.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getEmail()));
        crud_col_gender.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getGender()));
        crud_col_birthday.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBirthday().toString()));
        crud_col_status.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue().isStatus()).asString());

        List<Customer> listRemove = new ArrayList<>();
        for (Customer item : customerShowData) {
            if (!item.isStatus()) listRemove.add(item);
        }
        customerShowData.removeAll(listRemove);

        crud_tableView.setItems(customerShowData);

        // search function

        FilteredList<Customer> filteredList = new FilteredList<>(customerShowData, p -> true);

        keywordFieldText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customerSearchModel -> {

                // if no search value then display all records
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                // search value have to set DB not null
                String searchKeyword = newValue.toLowerCase();
//                if (customerSearchModel.getId_customer().toLowerCase().indexOf(searchKeyword)>-1){
//                    return true;
//                } else
                if (customerSearchModel.getFullname().toLowerCase().indexOf(searchKeyword) >-1) {
                    return true;
//                }else if (customerSearchModel.getGender().toLowerCase().indexOf(searchKeyword) >-1) {
//                    return true;
                }else if (customerSearchModel.getEmail().toLowerCase().indexOf(searchKeyword) >-1) {
                    return true;
                }else if (customerSearchModel.getUsername().toLowerCase().indexOf(searchKeyword) >-1) {
                    return true;
                } else {
                    return false; //no match found
                }
            });
        });

        SortedList<Customer> sortedList = new SortedList<>(filteredList);

        // bind sorted result with table view
        sortedList.comparatorProperty().bind(crud_tableView.comparatorProperty());

        crud_tableView.setItems(sortedList);

    }


    public void customerSelectData() {
        Customer customer = crud_tableView.getSelectionModel().getSelectedItem();
        LocalDate date = LocalDate.parse(customer.getBirthday().toString());


        crud_fullname.setText(customer.getFullname());
        crud_username.setText(customer.getUsername());
        crud_email.setText(customer.getEmail());
        crud_birthday.setValue(date);
//        crud_gender.setPromptText(customer.getGender());

    }

    public void initData(User user) {
        crud_admin.setText(user.getUsername());
    }

    public void logoutAccount() throws IOException {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout");
        if (alert.showAndWait().get() == ButtonType.OK) {

            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
//
            switchToSceneLogin();
        }


    }

    public void switchToSceneLogin() throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("loginForm.fxml"));
        stage = new Stage();
        Parent root = fxmlLoader.load();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerGender();
        try {
            customerShowData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // disable button add
        {
            BooleanBinding validateEmail = Bindings.createBooleanBinding(() -> {
                return crud_email.getText().matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");

            }, crud_email.textProperty());

            BooleanBinding validateUserName = Bindings.createBooleanBinding(() -> {
                return crud_username.getText().matches("[a-z0-9_-]{6,12}$");
            }, crud_username.textProperty());

            BooleanBinding validateFullname = Bindings.createBooleanBinding(() -> {
                return crud_fullname.getText().matches("^[a-z ]{2,29}$");
            }, crud_fullname.textProperty());

            crud_addBtn.disableProperty().bind(validateEmail.not().or(validateFullname.not()).or(validateUserName.not()));
        }

    }


}
