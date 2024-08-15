package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DashFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbTitle;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContactNumber;

    @FXML
    private TableColumn<Customer, LocalDate> colDOB;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private DatePicker dateDOB;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContactNumber;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    private List<Customer> customerList;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (isInputValid()) {
            Customer newCustomer = new Customer(
                    txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContactNumber.getText(),
                    dateDOB.getValue(),
                    cmbTitle.getValue()
            );

            if (findCustomerById(newCustomer.getId()) != null) {
                showAlert("Error", "Customer ID already exists.");
                return;
            }

            customerList.add(newCustomer);
            loadTable();
            clearText();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            customerList.remove(selectedCustomer);
            loadTable();
            clearText();
        } else {
            showAlert("Error", "Please select a customer to delete.");
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchId = txtId.getText();
        Customer foundCustomer = findCustomerById(searchId);
        if (foundCustomer != null) {
            populateFields(foundCustomer);
        } else {
            showAlert("Not Found", "Customer with ID " + searchId + " not found.");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null && isInputValid()) {
            int index = customerList.indexOf(selectedCustomer);
            customerList.set(index, new Customer(
                    txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContactNumber.getText(),
                    dateDOB.getValue(),
                    cmbTitle.getValue()
            ));

            loadTable();
            clearText();
        } else {
            showAlert("Error", "Please select a customer to update and ensure all fields are filled.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerList = DBConnection.getInstance().getConnection();

        ObservableList<String> title = FXCollections.observableArrayList("Mr.", "Master", "Mrs.", "Miss");
        cmbTitle.setItems(title);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });

        loadTable();
        addData();
    }

    private void clearText(){
        txtId.setText(null);
        txtName.setText(null);
        txtAddress.setText(null);
        txtContactNumber.setText(null);
        dateDOB.setValue(null);
        cmbTitle.setValue(null);
    }

    private void loadTable() {
        tblCustomers.setItems(FXCollections.observableArrayList(customerList));
    }
    private void addData(){
        customerList.add(new Customer("C001","Mithuni","Kadawatha","07189444427",LocalDate.of(2003,6,21),"Miss "));
        customerList.add(new Customer("C002","Anudi","Mahara","0716533421",LocalDate.of(2003,8,2),"Mrs."));
        customerList.add(new Customer("C003","Saman","Galle","0718124927",LocalDate.of(1997,9,12),"Mr."));
        customerList.add(new Customer("C004","Deshani","Ragama","07189444427",LocalDate.of(2004,7,5),"Miss "));
        customerList.add(new Customer("C005","Udayanka","Kidelpitiya","07189444427",LocalDate.of(2005,1,11),"Master "));
        customerList.add(new Customer("C006","Anuda","Horana","07189444427",LocalDate.of(2001,9,2),"Mr."));
        customerList.add(new Customer("C007","Adithya","Weyangoda","07189444427",LocalDate.of(2003,8,11),"Master "));
        customerList.add(new Customer("C008","Perera","Kegalle","07189444427",LocalDate.of(1993,3,2),"Mr."));
        customerList.add(new Customer("C009","Kamali","Anuradhapura","07189444427",LocalDate.of(2003,5,29),"Mrs."));
        customerList.add(new Customer("C010","Nimal","Panadura","07189444427",LocalDate.of(1999,2,17),"Mr."));
    }

    private void populateFields(Customer customer) {
        txtId.setText(customer.getId());
        String[] nameParts = customer.getName().split(" ", 2);
        if (nameParts.length > 1) {
            cmbTitle.setValue(nameParts[0]);
            txtName.setText(nameParts[1]);
        } else {
            txtName.setText(customer.getName());
        }
        txtAddress.setText(customer.getAddress());
        txtContactNumber.setText(customer.getPhoneNumber());
        dateDOB.setValue(customer.getBirthday());
    }

    private boolean isInputValid() {
        if (txtId.getText().isEmpty() || txtName.getText().isEmpty() ||
                txtAddress.getText().isEmpty() || txtContactNumber.getText().isEmpty() ||
                dateDOB.getValue() == null || cmbTitle.getValue() == null) {
            showAlert("Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        System.out.println(title + ": " + content);
    }

    private Customer findCustomerById(String id) {
        return customerList.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}