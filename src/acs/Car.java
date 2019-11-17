/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author PETER-PC
 */
public class Car extends Pane{
    public enum vehicleColor {BLACK, GREEN, RED, SKYBLUE, LIGHTLAVENDAR, LAVENDAR, LIGHTGREY, WHITE}
    private vehicleColor primaryColor;
    private vehicleColor secondaryColor;
    private String RFID;
    private vehicleAccess accessLevel;
    public enum vehicleAccess {Residence, Visitor};
    private String plateNumber;
    private String allocateSpace;
    public enum occupanyStatus {UNPARKED, PARKED};
    private occupanyStatus parkedStatus;
    
    private ImageView carIcon;
    private String vehicleModel;

    public String getAllocateSpace() {
        return allocateSpace;
    }

    public void setAllocateSpace(String allocateSpace) {
        this.allocateSpace = allocateSpace;
    }

    public occupanyStatus getParkedStatus() {
        return parkedStatus;
    }

    public void setParkedStatus(occupanyStatus parkedStatus) {
        this.parkedStatus = parkedStatus;
    }
    private ImageView rfidTag;

    public vehicleColor getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(vehicleColor primaryColor) {
        this.primaryColor = primaryColor;
    }

    public vehicleColor getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(vehicleColor secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public vehicleAccess getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(vehicleAccess accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public ImageView getCarIcon() {
        return carIcon;
    }

    public void setCarIcon(ImageView carIcon) {
        this.carIcon = carIcon;
    }

    public ImageView getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(ImageView rfidTag) {
        this.rfidTag = rfidTag;
    }
    
    public Car(String imgFileName){
        vehicleModel = imgFileName;
        carIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/"+imgFileName+".png")));
        rfidTag = new ImageView(new Image(getClass().getResourceAsStream("assets/RFID Sensor 2.png")));
        rfidTag.setFitWidth(10);
        rfidTag.setPreserveRatio(true);
        rfidTag.setLayoutX(20);
        rfidTag.setLayoutY(8);
        generateAccessLevel();
        parkedStatus = occupanyStatus.UNPARKED;
        allocateSpace = "";
        getChildren().addAll(carIcon, rfidTag);
        
        switch(imgFileName){
            case "Vehicle Coupe Grey":
                primaryColor = vehicleColor.LIGHTLAVENDAR;
                secondaryColor = vehicleColor.BLACK;
                RFID = generateCode(5);
                plateNumber = generateCode(7);
                break;
            case "Vehicle Limosin White":
                primaryColor = vehicleColor.LIGHTGREY;
                secondaryColor = vehicleColor.BLACK;
                RFID = generateCode(5);
                plateNumber = generateCode(7);
                break;
            case "Vehicle Sedan Green":
                primaryColor = vehicleColor.GREEN;
                secondaryColor = vehicleColor.BLACK;
                RFID = generateCode(5);
                plateNumber = generateCode(7);
                break;
            case "Vehicle Truck RedWhite":
                primaryColor = vehicleColor.RED;
                secondaryColor = vehicleColor.WHITE;
                RFID = generateCode(5);
                plateNumber = generateCode(7);
                break;
            case "Vehicle Van Blue":
                primaryColor = vehicleColor.SKYBLUE;
                secondaryColor = vehicleColor.BLACK;
                RFID = generateCode(5);
                plateNumber = generateCode(7);
                break;
            case "Vehicle Van LightBlue":
                primaryColor = vehicleColor.LAVENDAR;
                secondaryColor = vehicleColor.BLACK;
                RFID = generateCode(5);
                plateNumber = generateCode(7);
                break;
        }
         
        
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
    
    
    public final String generateCode(int codeLength){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < codeLength; i++){
            if(i % 2 == 0){
                int charCode = 65 + (int)(Math.random() * 26);
                builder.append(Character.toChars(charCode)[0]);
            }else{
                int numCode = (int)(Math.random() * 10);
                builder.append(numCode);
            }
        }
        return builder.toString();
    }

    public final void generateAccessLevel(){
        int random = (int)(Math.random() * 2);
        accessLevel = (random == 0 ? vehicleAccess.Residence : vehicleAccess.Visitor);
    }
    
    
}
