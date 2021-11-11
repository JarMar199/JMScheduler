package controller;

import DBConnect.DBQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    @FXML
    private TextField addressTxt;

    @FXML
    private TextField customerIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField postalTxt;

    @FXML
    private ComboBox<String> stateComboBox;

    @FXML
    private ComboBox<String> countryComboBox;


    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    void setCountryComboBox(ActionEvent event) throws SQLException {
        String selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        stateComboBox.setItems(DBQuery.getStates(selectedCountry));
    }

    public void sendCustomer(Customer customer) throws SQLException {
        customerIdTxt.setText(String.valueOf(customer.getId()));
        nameTxt.setText(customer.getName());
        addressTxt.setText(customer.getAddress());
        postalTxt.setText(customer.getPostal());
        phoneTxt.setText(customer.getPhone());
        countryComboBox.setValue(customer.getCountry());
        String selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        stateComboBox.setItems(DBQuery.getStates(selectedCountry));
        stateComboBox.setValue(customer.getState());
    }

    @FXML
    void onActionSaveCustomer(ActionEvent event) throws SQLException, IOException {
        int customerId = Integer.parseInt(customerIdTxt.getText());
        String name = nameTxt.getText();
        String address = addressTxt.getText();
        String postal = postalTxt.getText();
        String phone = phoneTxt.getText();
        String state = stateComboBox.getSelectionModel().getSelectedItem();
        //TODO
        if (DBQuery.modifyCustomer(customerId, name, address, postal, phone, state)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Confirmation");
            alert.setContentText("Customer successfully saved");
            alert.showAndWait();
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        } else
            System.out.println("Failed");


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            countryComboBox.setItems(DBQuery.getCountries());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}