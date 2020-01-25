 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 *
 * @author PETER-PC
 */
public class ACS extends Application {
    public static SplitPane cPanel;
    public static Pane root = new Pane();
    @Override
    public void start(Stage primaryStage) {

//            root = new Pane();
            root.setId("container");
            cPanel = new SplitPane();
            try {
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("ControlPanel.fxml"));

                cPanel = (SplitPane) myLoader.load();
                cPanel.setLayoutX(91);
                cPanel.setLayoutY(315);
                root.getChildren().add(cPanel);
                
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }     

            ImageView map = new ImageView(new Image(getClass().getResourceAsStream("assets/FloorPlan.png")));
            map.setFitHeight(550);
            map.setPreserveRatio(true);
            
            root.getChildren().add(0, map);
            
            primaryStage.setTitle("JavaFX PathTransition Test with SVG");

            Scene scene = new Scene(root, 740, 550);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            AnimationSequence animation = new AnimationSequence();
            
            root.getChildren().addAll(animation.getControlSystem());
            animation.setControlSystemCoordinates();
            
            scene.setOnMouseClicked(e ->{
                System.out.println("LayoutX: "+e.getSceneX()+"; LayoutY: "+e.getSceneY());
            });
            
            ((Button)cPanel.lookup("#btnResume")).setOnAction(e->{
                animation.playAnimation();
            });
            
            ((Button)cPanel.lookup("#btnPause")).setOnAction(e->{
                animation.pauseAnimation();
            });
//
            ((Button)cPanel.lookup("#btnStart")).setOnAction(e->{

                root.getChildren().removeIf((Node n)-> (n instanceof Car));
                if(!animation.getAnimationSequence().isEmpty())
                    animation.getAnimationSequence().clear();
                
                Thread t1 = new Thread(){
                    @Override
                        public void run(){
                            animation.generateParkedCars(getNOC_ParkedCarsValue());
//                            this.interrupt();
                            Platform.runLater(()->{
                                animation.layoutParkedCars(root);
                            });
                    }
                };

                
                Thread t2 = new Thread(){
                    @Override
                    public void run(){
                        if(getNOC_CheckIn() != 0)
                            animation.setNumOfCheckIn(getNOC_CheckIn());
                        if(getNOC_CheckOut() != 0 && !AnimationSequence.getParkedVehicles().isEmpty())
                            if(getNOC_CheckIn() != 0)
                                animation.setNumOfCheckOut(getNOC_CheckOut(), false);
                            else
                                animation.setNumOfCheckOut(getNOC_CheckOut(), true);

                    }
                };
                
                Thread t4 = new Thread(){
                    @Override
                    public void run(){
                        ToggleButton tgBtnVisitorLog = (ToggleButton) cPanel.lookup("#tgBtnVisitorLog");
                        tgBtnVisitorLog.setSelected(true);
                        TableView table = (TableView) cPanel.lookup("#tableView");
                        
                        Platform.runLater(()->{
                            ((TableColumn) table.getColumns().get(2)).setText("TIME-IN");
                            ((TableColumn) table.getColumns().get(3)).setText("TIME-OUT");
                        });
                        
                        if(!table.getItems().isEmpty())
                            table.getItems().clear();
                        table.getItems().addAll(animation.getVisitorLogTableData());
                    }
                };
                
                Thread t3 = new Thread(){
                    @Override
                    public void run(){
                        animation.generateVisitorLog();
                        if(getNOC_CheckIn() != 0)
                            animation.reconstructCheckInAnimation();
                        else if(!AnimationSequence.getCheckInVehicles().isEmpty())
                            AnimationSequence.getCheckInVehicles().clear();
                        animation.getAnimSequenceData();
                    }
                };
                
                ExecutorService executor = Executors.newFixedThreadPool(1);
                executor.execute(t1);
                executor.execute(t2);
                executor.execute(t3);
                executor.execute(t4);
                executor.shutdown();
                

            });
            
            ((Button)cPanel.lookup("#btnPlay")).setOnAction(e -> {
                if(getNOC_CheckIn() != 0 || getNOC_CheckOut() != 0 && !AnimationSequence.getParkedVehicles().isEmpty())
                    animation.playAnimationSequence();
                    });
            
            ((ToggleButton) cPanel.lookup("#tgBtnAnimSequence")).setOnAction(e-> animation.setToggle(e));
            ((ToggleButton) cPanel.lookup("#tgBtnVisitorLog")).setOnAction(e-> animation.setToggle(e));

    }
    
    public final int getNOC_ParkedCarsValue(){
        return ((int)((Slider)cPanel.lookup("#sldParkedCars")).getValue());
    }
    
    public final int getNOC_CheckIn(){
        return ((int)((ChoiceBox)cPanel.lookup("#cBoxCheckIn")).getValue());
    }
    
    public final int getNOC_CheckOut(){
        return ((int)((ChoiceBox)cPanel.lookup("#cBoxCheckOut")).getValue());
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}