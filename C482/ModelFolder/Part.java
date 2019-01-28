/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482.ModelFolder;

/**
 * @author celestet
 */
import javafx.beans.property.*;

public abstract class Part {
    private final IntegerProperty partID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;

    // Setters, contructors
    public Part() {
        partID = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        inStock = new SimpleIntegerProperty();
        price = new SimpleDoubleProperty();
        min = new SimpleIntegerProperty();
        max = new SimpleIntegerProperty();
    }

    // Getters, return each of the properties
    public IntegerProperty partIDproperty() {
        return partID;
    }
    public StringProperty partNameProperty() {
        return name;
    }
    public DoubleProperty partPriceProperty() {
        return price;
    }
    public IntegerProperty partInvProperty() {
        return inStock;
    }
    public IntegerProperty partMinProperty() {
        return min;
    }
    public IntegerProperty partMaxProperty() {
        return max;
    }
    public int getPartID() {
        return this.partID.get();
    }
    public String getPartName() {
        return this.name.get();
    }
    public double getPartPrice() {
        return this.price.get();
    }
    public int getPartInStock() {
        return this.inStock.get();
    }
    public int getPartMin() {
        return this.min.get();
    }
    public int getPartMax() {
        return this.max.get();
    }
    // Setters set each part's dimensions and information
    public void setPartID(int partID) {
        this.partID.set(partID);
    }
    public void setPartName(String name) {
        this.name.set(name);
    }
    public void setPartPrice(double price) {
        this.price.set(price);
    }
    public void setPartInStock(int inStock) {
        this.inStock.set(inStock);
    }
    public void setPartMin(int min) {
        this.min.set(min);
    }
    public void setPartMax(int max) {
        this.max.set(max);
    }
    // Validation whether each field exists, meets certain parameters
    public static String isPartValid(String name, int min, int max, int inv, double price, String errorMessage){
        if (name == null) {
            errorMessage = errorMessage + "Please enter a name for this part.";
        }
        if (inv < 1) {
            errorMessage = errorMessage + "The inventory count must be greater than or equal to 1.";
        }
        if (price <= 0) {
            errorMessage = errorMessage + "Please enter a price greater than $0.";
        }
        if (max < min) {
            errorMessage = errorMessage + "Max count must be greater than the Min count.";
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + "Inventory must be between Min and Max values.";
        }
        return errorMessage;
    }
}
