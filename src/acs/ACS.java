 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 *
 * @author PETER-PC
 */
public class ACS extends Application {
    SplitPane cPanel;
    @Override
    public void start(Stage primaryStage) {

            Pane root = new Pane();
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
            
            AnimationSequence animation = new AnimationSequence();
//            animation.setAnimationSequence("extP_reverseFromB1","extP_toExtFromB1Reverse");
            //D7 blacklist
            
//            animation.setNumOfCheckIn(3);
            animation.setControlSystemCoordinates();
            root.getChildren().addAll(animation.getControlSystem());

            root.getChildren().addAll(AnimationSequence.getCheckInVehicles());        

            primaryStage.setTitle("JavaFX PathTransition Test with SVG");
            Scene scene = new Scene(root, 740, 550);
            primaryStage.setScene(scene);
            primaryStage.show();

        
            ((Button)cPanel.lookup("#btnStart")).setOnAction(e->{
//                animation.playAnimationSequence(); 
                if(root.getChildren().containsAll(AnimationSequence.getParkedVehicles()))
                    root.getChildren().removeAll(AnimationSequence.getParkedVehicles());
                animation.generateParkedCars(getNOC_Value());
                animation.layoutParkedCars(root);
            });

    }
    
    public final int getNOC_Value(){
        return ((int)((Slider)cPanel.lookup("#sldParkedCars")).getValue());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}