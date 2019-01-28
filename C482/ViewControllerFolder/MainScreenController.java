/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482.ViewControllerFolder;

import c482.ModelFolder.Part;
import c482.ModelFolder.Product;
import c482.ModelFolder.Inventory;
import static c482.ModelFolder.Inventory.deletePart;
import static c482.ModelFolder.Inventory.getPartInventory;
import static c482.ModelFolder.Inventory.getProductInventory;
import static c482.ModelFolder.Inventory.removeProduct;
import static c482.ModelFolder.Inventory.validatePartDelete;
import static c482.ModelFolder.Inventory.validateProductDelete;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

/**
 * FXML Controller class
 * Main Screen
 * @author celestet
 */
public class MainScreenController implements Initializable {

    @FXML
    private TableView<Part> tvParts;
    @FXML
    private TableColumn<Part, Integer> tvPartsIDColumn;
    @FXML
    private TableColumn<Part, String> tvPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> tvPartsInvColumn;
    @FXML
    private TableColumn<Part, Double> tvPartsPriceColumn;
    @FXML
    private TableView<Product> tvProducts;
    @FXML
    private TableColumn<Product, Integer> tvProductsIDColumn;
    @FXML
    private TableColumn<Product, String> tvProductsNameColumn;
    @FXML
    private TableColumn<Product, Integer> tvProductsInvColumn;
    @FXML
    private TableColumn<Product, Double> tvProductsPriceColumn;
    @FXML
    private TextField txtSearchParts;
    @FXML
    private TextField txtSearchProducts;


    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }
    // Parts
    @FXML
    private void partSearchClearBtn(ActionEvent event) {
        updatePartsTableView();
        txtSearchParts.setText("");
    }
    @FXML
    private void searchParts(ActionEvent event) {
        String searchPart = txtSearchParts.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> temp = FXCollections.observableArrayList();
            temp.add(tempPart);
            tvParts.setItems(temp);
        }
    }
    @FXML
    private void deleteParts(ActionEvent event) {
        Part part = tvParts.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Error");
            alert.setHeaderText("Did not delete");
            alert.setContentText("Part is in use!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartsTableView();
                System.out.println("Part " + part.getPartName() + " was deleted.");
            }
            else {
                System.out.println("Part " + part.getPartName() + " was not deleted.");
            }
        }
    }
    @FXML
    private void launchAddPartScreen(ActionEvent event) throws IOException {
        Parent addPartParent = FXMLLoader.load(getClass().getResource("AddPartScreen.fxml"));
        Scene addPartScene = new Scene(addPartParent);
        Stage addPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addPartStage.setScene(addPartScene);
        addPartStage.show();
    }
    @FXML
    private void launchModifyPartScreen(ActionEvent event) throws IOException {
        modifyPart = tvParts.getSelectionModel().getSelectedItem();
        modifyPartIndex = getPartInventory().indexOf(modifyPart);
        Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyPartScreen.fxml"));
        Scene modifyPartScene = new Scene(modifyPartParent);
        Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyPartStage.setScene(modifyPartScene);
        modifyPartStage.show();
    }
    @FXML
    private void handlePartsSearch(ActionEvent event) {
        String searchPart = txtSearchParts.getText();
        int partIndex = -1;
        if (Inventory.lookupPart(searchPart) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        }
        else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            tvParts.setItems(tempPartList);
        }
    }    
    @FXML
    private void handlePartsDelete(ActionEvent event) {
        Part part = tvParts.getSelectionModel().getSelectedItem();
        if (validatePartDelete(part)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Part cannot be deleted!");
            alert.setContentText("Part is being used by one or more products.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Deletion");
            alert.setHeaderText("Confirm?");
            alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(part);
                updatePartsTableView();
                System.out.println("Part " + part.getPartName() + " was removed.");
            }
            else {
                System.out.println("Part " + part.getPartName() + " was not removed.");
            }
        }
    }
    @FXML
    private void clearPartsSearch(ActionEvent event) {
        updatePartsTableView();
        txtSearchParts.setText("");
    }

    // Products
    @FXML
    private void handleProductsSearch(ActionEvent event) {
        String searchProduct = txtSearchProducts.getText();
        int prodIndex = -1;
        if (Inventory.lookupProduct(searchProduct) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered cannot be found.");
            alert.showAndWait();
        }
        else {
            prodIndex = Inventory.lookupProduct(searchProduct);
            Product tempProduct = Inventory.getProductInventory().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            tvProducts.setItems(tempProductList);
        }
    }
    @FXML
    private void clearProductsSearch(ActionEvent event) {
        updateProductsTableView();
        txtSearchProducts.setText("");
    }
    @FXML
    private void searchProducts(ActionEvent event) {
        String searchProduct = txtSearchProducts.getText();
        int prodIndex = -1;
        if (Inventory.lookupProduct(searchProduct) == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match.");
            alert.showAndWait();
        }
        else {
            prodIndex = Inventory.lookupProduct(searchProduct);
            Product tempProduct = Inventory.getProductInventory().get(prodIndex);
            ObservableList<Product> tempProductList = FXCollections.observableArrayList();
            tempProductList.add(tempProduct);
            tvProducts.setItems(tempProductList);
        }
    }
    @FXML
    private void handleProductsDelete(ActionEvent event) {
        Product product = tvProducts.getSelectionModel().getSelectedItem();
        if (validateProductDelete(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Deleting");
            alert.setHeaderText("Error deleting");
            alert.setContentText("Product contains existing parts!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                removeProduct(product);
                updateProductsTableView();
                System.out.println("Product " + product.getProductName() + " was deleted.");
            } else {
                System.out.println("Product " + product.getProductName() + " was deleted.");
            }
        }
    }
    @FXML
    private void launchAddProductScreen(ActionEvent event) throws IOException {
        Parent addProductParent = FXMLLoader.load(getClass().getResource("AddProductScreen.fxml"));
        Scene addProductScene = new Scene(addProductParent);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene(addProductScene);
        addProductStage.show();
    }

    @FXML
    private void launchModifyProductScreen(ActionEvent event) throws IOException {
        modifyProduct = tvProducts.getSelectionModel().getSelectedItem();
        modifyProductIndex = getProductInventory().indexOf(modifyProduct);
        Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProductScreen.fxml"));
        Scene modifyProductScene = new Scene(modifyProductParent);
        Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modifyProductStage.setScene(modifyProductScene);
        modifyProductStage.show();
    }
    // Update the table views
    public void updatePartsTableView() {
        tvParts.setItems(getPartInventory());
    }

    public void updateProductsTableView() {
        tvProducts.setItems(getProductInventory());
    }
    // Confirm exit from Main Screen
    @FXML
    private void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Would you like to exit?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("You did not click to exit.");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvPartsIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDproperty().asObject());
        tvPartsInvColumn.setCellValueFactory(cellData -> cellData.getValue().partInvProperty().asObject());
        tvPartsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().partPriceProperty().asObject());
        tvPartsNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        tvProductsIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        tvProductsInvColumn.setCellValueFactory(cellData -> cellData.getValue().productInvProperty().asObject());
        tvProductsNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        tvProductsPriceColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        updatePartsTableView();
        updateProductsTableView();
    }
}
