/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482.ModelFolder;

/**
 * @author celestet
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private static int partIDCount = 0;
    private static int productIDCount = 0;

    public static ObservableList<Part> getPartInventory() {
        return partInventory;
    }
    public static void addPart(Part part) {
        partInventory.add(part);
    }
    public static void updatePart(int index, Part part) {
        partInventory.set(index, part);
    }
    public static void deletePart(Part part) {
        partInventory.remove(part);
    }
    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }
    public static boolean validatePartDelete(Part part) {
        boolean exists = false;
        for (int i = 0; i < productInventory.size(); i++) {
            if (productInventory.get(i).getProductParts().contains(part)) {
                exists = true;
            }
        }
        return exists;
    }
    public static boolean validateProductDelete(Product product) {
        boolean exists = false;
        int productID = product.getProductID();
        for (int i=0; i < productInventory.size(); i++) {
            if (productInventory.get(i).getProductID() == productID) {
                if (!productInventory.get(i).getProductParts().isEmpty()) {
                    exists = true;
                }
            }
        }
        return exists;
    }
    public static int lookupPart(String searchTerm) {
        boolean exists = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < partInventory.size(); i++) {
                if (Integer.parseInt(searchTerm) == partInventory.get(i).getPartID()) {
                    index = i;
                    exists = true;
                }
            }
        }
        else {
            for (int i = 0; i < partInventory.size(); i++) {
                searchTerm = searchTerm.toLowerCase();
                if (searchTerm.equals(partInventory.get(i).getPartName().toLowerCase())) {
                    index = i;
                    exists = true;
                }
            }
        }

        if (exists == true) {
            return index;
        }
        else {
            System.out.println("Could not find any parts.");
            return -1;
        }
    }
    public static ObservableList<Product> getProductInventory() {
        return productInventory;
    }
    public static void addProduct(Product product) {
        productInventory.add(product);
    }
    public static void removeProduct(Product product) {
        productInventory.remove(product);
    }
    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }
    public static int lookupProduct(String searchTerm) {
        boolean exists = false;
        int index = 0;
        if (isInteger(searchTerm)) {
            for (int i = 0; i < productInventory.size(); i++) {
                if (Integer.parseInt(searchTerm) == productInventory.get(i).getProductID()) {
                    index = i;
                    exists = true;
                }
            }
        }
        else {
            for (int i = 0; i < productInventory.size(); i++) {
                if (searchTerm.equals(productInventory.get(i).getProductName())) {
                    index = i;
                    exists = true;
                }
            }
        }
        if (exists == true) {
            return index;
        }
        else {
            System.out.println("Could not find your product.");
            return -1;
        }
    }
    public static void updateProduct(int index, Product product) {
        productInventory.set(index, product);
    }
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
