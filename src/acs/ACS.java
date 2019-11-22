 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
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
            


            primaryStage.setTitle("JavaFX PathTransition Test with SVG");
            Scene scene = new Scene(root, 740, 550);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            AnimationSequence animation = new AnimationSequence();
//            animation.setAnimationSequence("extP_reverseFromB1","extP_toExtFromB1Reverse");
            //D7 blacklist
            
            root.getChildren().addAll(animation.getControlSystem());
            animation.setControlSystemCoordinates();
            
            scene.setOnMouseClicked(e ->{
                System.out.println("LayoutX: "+e.getSceneX()+"; LayoutY: "+e.getSceneY());
            });

        
            ((Button)cPanel.lookup("#btnStart")).setOnAction(e->{
                if(root.getChildren().containsAll(AnimationSequence.getParkedVehicles()))
                    root.getChildren().removeAll(AnimationSequence.getParkedVehicles());
                new Thread(){
                    @Override
                        public void run(){
                            animation.generateParkedCars(getNOC_ParkedCarsValue());
//                            this.interrupt();
                            Platform.runLater(()->{
                                animation.layoutParkedCars(root);
                            });
                    }
                }.start();
                

                if(root.getChildren().containsAll(AnimationSequence.getCheckInVehicles()))
                    root.getChildren().removeAll(AnimationSequence.getCheckInVehicles());
                
                new Thread(){
                    @Override
                    public void run(){
                        animation.setNumOfCheckIn(getNOC_AccessRunsValue());
//                        animation.setNumOfCheckOut(getNOC_AccessRunsValue());
//                        interrupt();
                        Platform.runLater(()->{
                            root.getChildren().addAll(AnimationSequence.getCheckInVehicles());        
                        });
                    }
                }.start();
                
//                if(getNOC_AccessRunsValue() != 0)
//                    animation.playAnimationSequence(); 
            });
            
             ((Button)cPanel.lookup("#btnPlay")).setOnAction(e ->{
//                if(root.getChildren().containsAll(AnimationSequence.getParkedVehicles())){
//                    root.getChildren().removeAll(AnimationSequence.getParkedVehicles());
//                }
//                    
//                    animation.generateParkedCars(getNOC_ParkedCarsValue());
//                    animation.layoutParkedCars(root);
//                    
//                if(root.getChildren().containsAll(AnimationSequence.getCheckInVehicles())) {
//                    root.getChildren().removeAll(AnimationSequence.getCheckInVehicles());
//                }
//                
//                animation.setNumOfCheckOut(getNOC_AccessRunsValue());
//                Timer timer = new Timer();
//                TimerTask task = new TimerTask(){
//                    @Override
//                    public void run(){
//                        
//                    }
//                    
//                };
//                int output = 0;
//                for(Animation a: animation.getAnimationSequence())
//                    output+=5000;
//                        
//                timer.schedule(task, 3000);
//                timer.purge();
                if (getNOC_AccessRunsValue() != 0) {
                     animation.playAnimationSequence();
                }   
             });

    }
    
    public final int getNOC_ParkedCarsValue(){
        return ((int)((Slider)cPanel.lookup("#sldParkedCars")).getValue());
    }
    
    public final int getNOC_AccessRunsValue(){
        return ((int)((Slider)cPanel.lookup("#sldACS")).getValue());
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}