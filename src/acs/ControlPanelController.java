package acs;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;

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

        cBoxTimer.setItems(FXCollections.observableArrayList(1,2,3,4,5));
        cBoxTimer.setValue(3);
    }    
    
    @FXML
    private ChoiceBox cBoxTimer;
    
//    public static class Result {
//
//        private final SimpleStringProperty matricNo;
//        private final SimpleStringProperty department;
//        private final SimpleStringProperty fullName;
//        private final SimpleDoubleProperty score;
//
//        private Result(String matricNo, String department, String fullName, Double score) {
//            this.matricNo = new SimpleStringProperty(matricNo);
//            this.department = new SimpleStringProperty(department);
//            this.fullName = new SimpleStringProperty(fullName);
//            this.score = new SimpleDoubleProperty(score);
//        }
//
//        public String getMatricNo() {
//            return matricNo.get();
//        }
//
//        public String getDepartment() {
//            return department.get();
//        }
//
//        public String getFullName() {
//            return fullName.get();
//        }
//
//        public double getScore() {
//            return score.get();
//        }
//
//        public void setMatricNo(String matricNo) {
//            this.matricNo.set(matricNo);
//        }
//
//        public void setDepartment(String department) {
//            this.department.set(department);
//        }
//
//        public void setFullName(String fullName) {
//            this.fullName.set(fullName);
//        }
//
//        public void setScore(double score) {
//            this.score.set(score);
//        }
//
//    }
//    
//    class EditingCell extends TableCell<Result, Double> {
//
//        private TextField textField;
//
//        public EditingCell() {
//        }
//
//        @Override
//        public void startEdit() {
//            super.startEdit();
//
//            if (textField == null) {
//                createTextField();
//            }
//
//            setGraphic(textField);
//            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//            textField.selectAll();
//        }
//
//        @Override
//        public void cancelEdit() {
//            super.cancelEdit();
//
//            setText(String.valueOf(getItem()));
//            setContentDisplay(ContentDisplay.TEXT_ONLY);
//        }
//
//        @Override
//        public void updateItem(Double item, boolean empty) {
//            super.updateItem(item, empty);
//
//            if (empty) {
//                setText(null);
//                setGraphic(null);
//            } else if (isEditing()) {
//                if (textField != null) {
//                    textField.setText(getString());
//                }
//                setGraphic(textField);
//                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//            } else {
//                setText(getString());
//                setContentDisplay(ContentDisplay.TEXT_ONLY);
//                try {
//                    updateRecord(getString());
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//
//        private void updateRecord(String result) throws SQLException {
//            String queryString = "update Result set ResultScore = ? where ResultCourseCode = ? and "
//                    + "ResultMatric = ? ";
//
//            statement = connection.prepareStatement(queryString);
//
//            statement.setString(1, result);
//            statement.setString(2, ((Label) menuSelection.getValue().getChildren().get(0)).getText());
//            statement.setString(3, matricNoColumn.getCellData(rowIndex) + "");
//            statement.executeUpdate();
//            System.out.println("Database updated at matricNo: " + matricNoColumn.getCellData(rowIndex)
//                    + " and Course: " + ((Label) menuSelection.getValue().getChildren().get(0)).getText());
//        }
//
//        private void createTextField() {
//            textField = new TextField(getString());
//            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//            textField.setOnKeyPressed((KeyEvent t) -> {
//                if (t.getCode() == KeyCode.ENTER) {
//                    commitEdit(Double.parseDouble(textField.getText()));
//                } else if (t.getCode() == KeyCode.ESCAPE) {
//                    cancelEdit();
//                }
//            });
//        }
//
//        private String getString() {
//            return getItem() == null ? "" : getItem().toString();
//        }
//    }
    
    @FXML
    private Slider sldAccessRuns;
    
    @FXML
    private Slider sldParkedCars;
}
