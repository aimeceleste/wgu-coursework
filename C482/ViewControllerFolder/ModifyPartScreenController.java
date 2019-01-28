/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482.ViewControllerFolder;

import c482.ModelFolder.InHousePart;
import c482.ModelFolder.Inventory;
import static c482.ModelFolder.Inventory.getPartInventory;
import c482.ModelFolder.OutsourcedPart;
import c482.ModelFolder.Part;
import static c482.ViewControllerFolder.MainScreenController.partToModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 * Modify Part Screen
 * @author celestet
 */
public class ModifyPartScreenController implements Initializable {

    private boolean isOutsourced;
    int partIndex = partToModifyIndex();
    private String exceptionMessage = new String();
    private int partID;

    @FXML
    private RadioButton radioModifyPartInHouse;
    @FXML
    private RadioButton radioModifyPartOutsourced;
    @FXML
    private Label lblModifyPartIDNumber;
    @FXML
    private TextField txtModifyPartName;
    @FXML
    private TextField txtModifyPartInv;
    @FXML
    private TextField txtModifyPartPrice;
    @FXML
    private TextField txtModifyPartMin;
    @FXML
    private TextField txtModifyPartMax;
    @FXML
    private Label lblModifyPartDyn;
    @FXML
    private TextField txtModifyPartDyn;

    @FXML
    void modifyPartInHouseRadio(ActionEvent event) {
        isOutsourced = false;
        radioModifyPartOutsourced.setSelected(false);
        lblModifyPartDyn.setText("Machine ID");
        txtModifyPartDyn.setText("");
        txtModifyPartDyn.setPromptText("Machine ID");
    }

    @FXML
    void modifyPartOutsourcedRadio(ActionEvent event) {
        isOutsourced = true;
        radioModifyPartInHouse.setSelected(false);
        lblModifyPartDyn.setText("Company Name");
        txtModifyPartDyn.setText("");
        txtModifyPartDyn.setPromptText("Company Name");
    }

    @FXML
    void saveModifyParts(ActionEvent event) throws IOException {
        String partName = txtModifyPartName.getText();
        String partInv = txtModifyPartInv.getText();
        String partPrice = txtModifyPartPrice.getText();
        String partMin = txtModifyPartMin.getText();
        String partMax = txtModifyPartMax.getText();
        String partDyn = txtModifyPartDyn.getText();

        try {
            exceptionMessage = Part.isPartValid(partName, Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partInv), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (isOutsourced == false) {
                    System.out.println("Part name: " + partName);
                    InHousePart inPart = new InHousePart();
                    inPart.setPartID(partID);
                    inPart.setPartName(partName);
                    inPart.setPartInStock(Integer.parseInt(partInv));
                    inPart.setPartPrice(Double.parseDouble(partPrice));
                    inPart.setPartMin(Integer.parseInt(partMin));
                    inPart.setPartMax(Integer.parseInt(partMax));
                    inPart.setPartMachineID(Integer.parseInt(partDyn));
                    Inventory.updatePart(partIndex, inPart);
                }
                else {
                    System.out.println("Part name: " + partName);
                    OutsourcedPart outPart = new OutsourcedPart();
                    outPart.setPartID(partID);
                    outPart.setPartName(partName);
                    outPart.setPartInStock(Integer.parseInt(partInv));
                    outPart.setPartPrice(Double.parseDouble(partPrice));
                    outPart.setPartMin(Integer.parseInt(partMin));
                    outPart.setPartMax(Integer.parseInt(partMax));
                    outPart.setPartCompanyName(partDyn);
                    Inventory.updatePart(partIndex, outPart);
                }

                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Part");
            alert.setContentText("Form contains blank fields.");
            alert.showAndWait();
        }
    }
    @FXML
    // cancel button event for modify part 
    private void cancelModifyParts(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to cancel modifying the part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent modifyPartCancel = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(modifyPartCancel);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
        else {
            System.out.println("You clicked cancel.");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();
        lblModifyPartIDNumber.setText("Auto-Gen: " + partID);
        txtModifyPartName.setText(part.getPartName());
        txtModifyPartInv.setText(Integer.toString(part.getPartInStock()));
        txtModifyPartPrice.setText(Double.toString(part.getPartPrice()));
        txtModifyPartMin.setText(Integer.toString(part.getPartMin()));
        txtModifyPartMax.setText(Integer.toString(part.getPartMax()));
        if (part instanceof InHousePart) {
            lblModifyPartDyn.setText("Machine ID");
            txtModifyPartDyn.setText(Integer.toString(((InHousePart) getPartInventory().get(partIndex)).getPartMachineID()));
            radioModifyPartInHouse.setSelected(true);
        }
        else {
            lblModifyPartDyn.setText("Company Name");
            txtModifyPartDyn.setText(((OutsourcedPart) getPartInventory().get(partIndex)).getPartCompanyName());
            radioModifyPartOutsourced.setSelected(true);
        }
    }
}