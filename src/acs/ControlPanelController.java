package acs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Labeled;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author PETER-PC
 */
public class ControlPanelController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Tooltip tooltipSldParkedCars = new Tooltip();
        Tooltip.install(sldParkedCars, tooltipSldParkedCars);
        sldParkedCars.valueProperty().addListener(new ChangeListener(){
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                tooltipSldParkedCars.setText(newValue.toString());
            }
        });
        
        sldParkedCars.setValue(5);

        cBoxTimer.setItems(FXCollections.observableArrayList(1,2,3,4,5));
        cBoxTimer.setValue(3);

        cBoxCheckIn.setItems(FXCollections.observableArrayList(0,1,2,3,4,5));
        cBoxCheckIn.setValue(4);

        cBoxCheckOut.setItems(FXCollections.observableArrayList(0,1,2,3,4,5));
        cBoxCheckOut.setValue(4);
        
        ToggleGroup tGroup = new ToggleGroup();
        tgBtnVisitorLog.setToggleGroup(tGroup);
        tgBtnAnimSequence.setToggleGroup(tGroup);
        tgBtnVisitorLog.setSelected(true);
        
        ((TableColumn) tableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("Index"));
        ((TableColumn) tableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("RFID"));
        ((TableColumn) tableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("Activity_TimeIn"));
        ((TableColumn) tableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("Time_TimeOut"));
        ((TableColumn) tableView.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<>("Status"));
        
        tableView.setRowFactory(row -> new TableRow<AnimationSequence.AnimPlan_VisitorLog>(){
            @Override
            public void updateItem(AnimationSequence.AnimPlan_VisitorLog item, boolean isCheckInCar){
                super.updateItem(item, isCheckInCar);
//                System.out.println("Testing");
                if(item == null || isCheckInCar){
                    setText(null);
                }else{
                    for(Car c: AnimationSequence.getCheckInVehicles()){
                        if(c.getRFIDNumber().getText().matches(item.getRFID()) && item.getActivity_TimeIn().matches("[\\d]{2}:[\\d]{2}:[\\d]{2}")){
                            for(int i=0; i < getChildren().size(); i++){
                                ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: grey; -fx-text-fill: white; -fx-alignment: center");
                            }
                        }
//                        else{
//                            for(int i=0; i < getChildren().size(); i++){
//                                ((Labeled)getChildren().get(i)).setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-alignment: center");
//                            }
//                        }
                    }
                }
            }
        });
    }
       
    @FXML
    private TableView tableView;
    @FXML
    private ToggleButton tgBtnVisitorLog;
    
    @FXML
    private ToggleButton tgBtnAnimSequence;
    
    @FXML
    private Slider sldParkedCars;
    
    @FXML
    private ChoiceBox cBoxTimer;
    
    @FXML
    private ChoiceBox cBoxCheckIn;
    
    @FXML
    private ChoiceBox cBoxCheckOut;
    
}
