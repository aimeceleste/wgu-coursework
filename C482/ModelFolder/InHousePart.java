/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c482.ModelFolder;

/**
 *
 * @author celestet
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InHousePart extends Part {
    private final IntegerProperty partMachineID;
    public InHousePart() {
        super();
        partMachineID = new SimpleIntegerProperty();
    }
    public void setPartMachineID(int partMachineID) {
        this.partMachineID.set(partMachineID);
    }
    public int getPartMachineID() {
        return this.partMachineID.get();
    }

}
