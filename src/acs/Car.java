/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author PETER-PC
 */
public class Car extends Pane{
    public enum vehicleColor {BLACK, GREEN, RED, SKYBLUE, LIGHTLAVENDAR, LAVENDAR, LIGHTGREY, WHITE}
    private vehicleColor primaryColor;
    private vehicleColor secondaryColor;
    private Label rfidNumber;
    private vehicleAccess accessLevel;
    public enum vehicleAccess {Residence, Visitor};
//    private Label plateNumber;
    private String allocateSpace;
    public enum occupanyStatus {UNPARKED, PARKED};
    private occupanyStatus parkedStatus;
    
    private ImageView carIcon;
    private String vehicleModel;
    private ImageView rfidTag;

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

    public Label getRFIDNumber() {
        return rfidNumber;
    }

    public void setRFIDNumber(String rfidNumber) {
        this.rfidNumber.setText(rfidNumber);
    }

    public vehicleAccess getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(vehicleAccess accessLevel) {
        this.accessLevel = accessLevel;
    }
//
//    public Label getPlateNumber() {
//        return plateNumber;
//    }
//
//    public void setPlateNumber(String plateNumber) {
//        this.plateNumber.setText(plateNumber);
//    }

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
    
    private Rectangle cursor;
    
    public Car(){
        this("Vehicle Sedan Green");
    }
    
    public Car(String imgFileName){
        vehicleModel = imgFileName;
        carIcon = new ImageView(new Image(getClass().getResourceAsStream("assets/"+imgFileName+".png")));
        rfidTag = new ImageView(new Image(getClass().getResourceAsStream("assets/RFID Sensor 2.png")));
        generateAccessLevel();
        allocateSpace = "";
        parkedStatus = occupanyStatus.UNPARKED;
       
        
        translateXProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                System.out.print("\n"+getVehicleModel()+" parked at "+getAllocateSpace()+" is @ X: "+newValue);
            }
        });
        
        translateYProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                System.out.println(" & Y: "+newValue);
//                System.out.println("Node Orientation of Car is "+getNodeOrientation()+" & Node Orientation of Car Icon is "+carIcon.getNodeOrientation());
            }
        });
        
        switch(imgFileName){
            case "Vehicle Coupe Grey":
                primaryColor = vehicleColor.LIGHTLAVENDAR;
                secondaryColor = vehicleColor.BLACK;
                rfidNumber = new Label(generateCode(4));
//                plateNumber = new Label(generateCode(6));
                break;
            case "Vehicle Limosin White":
                primaryColor = vehicleColor.LIGHTGREY;
                secondaryColor = vehicleColor.BLACK;
                rfidNumber = new Label(generateCode(4));
//                plateNumber = new Label(generateCode(6));
                break;
            case "Vehicle Sedan Green":
                primaryColor = vehicleColor.GREEN;
                secondaryColor = vehicleColor.BLACK;
                rfidNumber = new Label(generateCode(4));
//                plateNumber = new Label(generateCode(6));
                break;
            case "Vehicle Truck RedWhite":
                primaryColor = vehicleColor.RED;
                secondaryColor = vehicleColor.WHITE;
                rfidNumber = new Label(generateCode(4));
//                plateNumber = new Label(generateCode(6));
                break;
            case "Vehicle Van Blue":
                primaryColor = vehicleColor.SKYBLUE;
                secondaryColor = vehicleColor.BLACK;
                rfidNumber = new Label(generateCode(4));
//                plateNumber = new Label(generateCode(6));
                break;
            case "Vehicle Van LightBlue":
                primaryColor = vehicleColor.LAVENDAR;
                secondaryColor = vehicleColor.BLACK;
                rfidNumber = new Label(generateCode(4));
//                plateNumber = new Label(generateCode(6));
                break;
        }
        
        rfidTag.setFitWidth(10);
        rfidTag.setPreserveRatio(true);
        rfidNumber.setStyle("-fx-background-color: rgba(50,50,50,0.6); -fx-border-color: darkgrey; -fx-text-fill: white; -fx-margin: 0; -fx-padding: 0;");
        getChildren().addAll(carIcon,rfidTag,rfidNumber);
        rfidTag.setLayoutX(carIcon.getBoundsInLocal().getWidth() / 2f);
        rfidTag.setLayoutY(10);
//        
        rfidNumber.setLayoutY(4);
        rfidNumber.setTranslateX(carIcon.getBoundsInLocal().getWidth() - 15);
        rfidNumber.setRotate(90);
        
        cursor = new Rectangle(carIcon.getBoundsInLocal().getHeight() + 3, carIcon.getBoundsInLocal().getWidth() + 3);
    }
    
    public void adjustRFIDLocation(){
        rfidNumber.setTranslateX(-15);
    }
    
    class Tracker implements ChangeListener{
        int refreshRate = 0;
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            ++refreshRate;
            if(refreshRate % 5 == 0){
                int deltaX = 5 - (int) (Math.random() * 10);
                int deltaY = 5 - (int) (Math.random() * 10);
                cursor.setTranslateX(15 + carIcon.getTranslateX() + deltaX);
                cursor.setTranslateY(carIcon.getTranslateY() - 15 + deltaY);
            }else{
                cursor.setTranslateX(18 + carIcon.getTranslateX());
                cursor.setTranslateY(carIcon.getTranslateY() - 15);
            }
        }
    }
    
    Tracker t;
    
    public void startTracking(){
        cursor.setFill(Color.rgb(255, 199, 0));
        cursor.setRotate(90);
        Platform.runLater(()-> {
            getChildren().add(0, cursor);
                });
        t = new Tracker();
        translateYProperty().addListener(t);
    }
    
    public void endTracking(){
        translateYProperty().removeListener(t);
        if(getChildren().contains(cursor))
            Platform.runLater(()-> getChildren().remove(cursor));
    }
//    
//    public void Track(){
//        
//    }

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
