/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;

import static acs.ACS.cPanel;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.parser.PathParser;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author PETER-PC
 */
public class AnimationSequence {
    private ArrayList<Animation> sT;
    private SVGDocument svgEntranceDrivePath, svgExitDrivePath, svgDrivePath;
    private static double[][] animationRate = {
        {1,.95,.85,.75,.65}, 
        {1,1,.95,.95,.85,.85,.75,.75,.65,.65,.5}, 
        {1,1,.95,.95,.85,.85,.75,.75,.65,.65,.5}, 
        {.85,.85,.85,.75,.75,.75,.65,.65,.65,.5,.5}, 
        {.5,.5,.5,.5,.5},
        {1,1,1,1,1},
        {.5, .5, .5, .5, .5},
        {.65, .65, .75, .75, .80, .85, .85, .85, 1, 1, 1.15},
        {.5, .5, .65, .65, .65, .75, .75, .75, .85, .85, .85},
        {.5, .5, .65, .65, .65, .75, .75, .75, .85, .85, .85},
        {1.45, 1.35, 1.25, 1.2, 1.1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1.25, 1.25, 1.25, 1.25, 1.25}};
//    private Node car;
    private ArrayList<Node> VACS_Components;
    
    
    public AnimationSequence(){
//        this.car = car;
        PathParser parser = new PathParser();
        VACS_Components = new ArrayList<>();
        sT = new ArrayList<>();
            
        JavaFXPathElementHandler handler = new JavaFXPathElementHandler();
        parser.setPathHandler(handler);
        try {
            String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(xmlParser);

            File entrancePath = new File("src/acs/assets/FloorPlan(Entrance Paths).svg");
            File exitPath = new File("src/acs/assets/FloorPlan(Exit Paths).svg");
            File drivePath = new File("src/acs/assets/FloorPlanDrivePath.svg");
            svgEntranceDrivePath = f.createSVGDocument(entrancePath.getAbsoluteFile().toURI().toString());
            svgExitDrivePath = f.createSVGDocument(exitPath.getAbsoluteFile().toURI().toString());
            svgDrivePath = f.createSVGDocument(drivePath.getAbsoluteFile().toURI().toString());
        }catch(IOException ex){}
        
    }
    
    public Path getPath(String pathID){
        PathParser parser = new PathParser();
        JavaFXPathElementHandler handler = new JavaFXPathElementHandler();
        parser.setPathHandler(handler);
        String pathData, matrixTransform;

//        SAXParser p = new SAXParser();
        Element selectedPath = (pathID.matches("entP_to[\\S]{2,}")? svgEntranceDrivePath.getElementById(pathID) : svgExitDrivePath.getElementById(pathID));
//        Element selectedPath = svgDrivePath.getElementById(pathID);
        pathData = selectedPath.getAttributeNode("d").getValue();
        matrixTransform = selectedPath.getAttributeNode("transform").getValue();
        
        parser.parse(pathData);
        Path path = handler.getPath();
        
        matrixTransform = matrixTransform.replace("matrix(", "");
        matrixTransform = matrixTransform.replace(")", "");
        String[] transforms = matrixTransform.split(",");
        path.getTransforms()
                .add(Transform.affine(Double.parseDouble(transforms[0]), Double.parseDouble(transforms[1]), Double.parseDouble(transforms[2]),
                        Double.parseDouble(transforms[3]), Double.parseDouble(transforms[4]), Double.parseDouble(transforms[5])));
        return path;
    }
    
    public double getAnimRateFromPathID(String pathID){
        int rowIndex = 0, columnIndex = 0; String output = ""; char c = '\u0000';
        if(pathID.matches("entP_to[\\S]{2,3}")){
                output = pathID.substring(7);
                c = output.charAt(0);
                columnIndex = Integer.parseInt(pathID.substring(8));
//                System.out.println("Column Index: "+columnIndex);
                switch(c){
                    case 'B':
                        rowIndex = 1;
                        break;
                    case 'C':
                        rowIndex = 2;
                        break;
                    case 'D':
                        rowIndex = 3;
                        break;
                    case 'E':
                        rowIndex = 4;
                        break;
                }
//                System.out.println("Row Index: "+rowIndex);

                return animationRate[rowIndex][--columnIndex];
        }else if(pathID.matches("entP_to[\\S]{2,}")){
                rowIndex = 5; 
                switch(pathID){
                    case "entP_toFirstBarrier":
                        columnIndex = 0;
                        break;
                    case "entP_toSecondBarrier":
                        columnIndex = 1;
                        break;
                    case "entP_toEntExt":
                        columnIndex = 2;
                        break;
                    case "entP_toExtExt":
                        columnIndex = 3;
                        break;
                    case "extP_toExtExt":
                        columnIndex = 4;
                        break;
                }

                return animationRate[rowIndex][columnIndex];
        }else if(pathID.matches("extP_toExtFrom[\\S]{2,}")){
                output = pathID.substring(14);
                c = output.charAt(0);
                rowIndex = 6;
                String columnIndexString = (pathID.length() > 17 ? pathID.substring(15, 17): pathID.substring(15));
               
                if(columnIndexString.matches("[\\d][\\D]"))
                    columnIndexString = columnIndexString.charAt(0)+"";
                
                columnIndex = Integer.parseInt(columnIndexString);
                switch(c){
                    case 'B':
                        rowIndex = 7;
                        break;
                    case 'C':
                        rowIndex = 8;
                        break;
                    case 'D':
                        rowIndex = 9;
                        break;
                    case 'E':
                        rowIndex = 10;
                        break;
                }

                return animationRate[rowIndex][--columnIndex];
        }else if(pathID.matches("extP_reverseFrom[\\S]{2,}")){
                output = pathID.substring(16);
                c = output.charAt(0);
                rowIndex = 11;
                columnIndex = Integer.parseInt(pathID.substring(17));
                switch (c) {
                    case 'B':
                        rowIndex = 12;
                        break;
                    case 'C':
                        rowIndex = 13;
                        break;
                    case 'D':
                        rowIndex = 14;
                        break;
                    case 'E':
                        rowIndex = 15;
                        break;
                }
                return animationRate[rowIndex][columnIndex - 1];
        }else
            return 1;
               
    }

    public final void setAnimationSequence(Node car, String... pathID) {
        ArrayList<Animation> tempAnimationList = new ArrayList<>();
        for(int i = 0; i < pathID.length; i++){
//            System.out.println("Get animation path for svg path id: "+pathID[i]);
            Path p = getPath(pathID[i]);
            if(pathID[i].matches("extP_[\\w]+")){
                p.setTranslateX(-708);
                p.setTranslateY(-200);
            }else
                p.setTranslateY(18);
                
            PathTransition pT = new PathTransition(Duration.seconds(5), p, car);
            pT.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pT.setInterpolator(Interpolator.EASE_BOTH);
            pT.setRate(getAnimRateFromPathID(pathID[i]));
            
            tempAnimationList.add(pT);
            
            ChoiceBox c = ((ChoiceBox)ACS.root.lookup("#cBoxTimer"));
            int barrierDuration = Integer.parseInt((c.getValue().toString()));
            
            int counter = 0;
            counter = tempAnimationList.stream().filter((a) -> (a instanceof ParallelTransition)).map((_item) -> 1).reduce(counter, Integer::sum);
            if (pathID[i].matches("entP_toFirstBarrier")){
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(0), Duration.seconds(barrierDuration), -45));
            }else if(pathID[i].matches("entP_toSecondBarrier")){
                ParallelTransition plT = new ParallelTransition(getBarrierAnimation(VACS_Components.get(0), Duration.seconds(barrierDuration), 0), tempAnimationList.remove(2), setNotificationAnimation("Vehicle Granted Access"));
                tempAnimationList.add(plT);
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(1), Duration.seconds(barrierDuration), -45));
            }else if(pathID[i].matches("entP_to[\\S]{2,3}") && (counter == 1)){
                ParallelTransition plT = new ParallelTransition(getBarrierAnimation(VACS_Components.get(1), Duration.seconds(barrierDuration), 0), tempAnimationList.remove(4), setNotificationAnimation("Vehicle Assigned Lot "+pathID[i].substring(7)));
                tempAnimationList.add(plT);

//                printOccupancyArray();
            }else if(pathID[i].matches("extP_toExtFrom[\\S]{2,}")){
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(3), Duration.seconds(barrierDuration), -45));
            }else if(pathID[i].matches("extP_toExtExt")){
                PathTransition exitPathTransition = (PathTransition) tempAnimationList.remove(tempAnimationList.size()-1);
                Path exitPath = (Path) exitPathTransition.getPath();
                exitPath.setTranslateX(getExitPathTranslations(((Car)car).getAllocateSpace()).getX());
                exitPath.setTranslateY(getExitPathTranslations(((Car)car).getAllocateSpace()).getY());
                ParallelTransition plT = new ParallelTransition(getBarrierAnimation(VACS_Components.get(3), Duration.seconds(barrierDuration), 0), exitPathTransition, 
                        setNotificationAnimation("Vehicle Exits Lot "+((Car)car).getAllocateSpace()));
                tempAnimationList.add(plT);
            }else if(pathID[i].matches("entP_toEntExt1")){
                ParallelTransition plT = new ParallelTransition(getBarrierAnimation(VACS_Components.get(0), Duration.seconds(barrierDuration), 0), tempAnimationList.remove(2), setNotificationAnimation("Vehicle Denied Access"));
                tempAnimationList.add(plT);
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(2), Duration.seconds(barrierDuration), 45));
            }else if(pathID[i].matches("entP_toEntExt2")){
                ParallelTransition plT = new ParallelTransition(getBarrierAnimation(VACS_Components.get(2), Duration.seconds(barrierDuration), 90), tempAnimationList.remove(4));
                tempAnimationList.add(plT);
            }
            
        }

        tempAnimationList.add(0, new PauseTransition(Duration.seconds(2)));
        tempAnimationList.add(new PauseTransition(Duration.seconds(2)));
        
        sT.addAll(tempAnimationList);
//        System.out.println("Animation contains: "+sT.size());
    }

    public ArrayList<Animation> getAnimationSequence() {
        return sT;
    }


    public Timeline setNotificationAnimation(String message) {
        Label notification = new Label();
        notification.setText(message);
        notification.setFont(new Font(36));
        notification.setTextFill(Color.WHITE);
        notification.setStyle("-fx-background-color: grey; -fx-border-color: black; -fx-padding: 0 5; -fx-border-radius: 5; -fx-background-radius: 5;");

        Timeline output = new Timeline();
        output.getKeyFrames().add(new KeyFrame(Duration.seconds(0), e-> {
            Platform.runLater(()-> {
                ACS.root.getChildren().add(notification);
                notification.setLayoutX(176);
                notification.setLayoutY(185);
                notification.setOpacity(0);
            });
        }));
        output.getKeyFrames().add(new KeyFrame(Duration.seconds(4), new KeyValue(notification.opacityProperty(), .6, Interpolator.EASE_OUT)));
        output.getKeyFrames().add(new KeyFrame(Duration.seconds(4), new KeyValue(notification.opacityProperty(), 0, Interpolator.EASE_IN)));
        output.getKeyFrames().add(new KeyFrame(Duration.seconds(5), e-> Platform.runLater(()-> ACS.root.getChildren().remove(notification))));

        return output;
    }
    
    public void playAnimationSequence(){
        for(Animation a: sT)
            System.out.println("Animation "+a);
            
        for(Animation a: sT){
            int currentIndex = sT.indexOf(a);
            
            if(currentIndex < (sT.size()-1)){
                
                Animation nextAnimation = sT.get(++currentIndex);
                
                if(nextAnimation instanceof ParallelTransition){
                    PathTransition currentPathTransition = ((PathTransition)((ParallelTransition) nextAnimation).getChildren().get(1));
                    Car c = (Car) currentPathTransition.getNode();
                    
                    Animation followUpAnimation = sT.get(currentIndex+1);
                    a.setOnFinished(e-> {

                        if (!ACS.root.getChildren().contains(c)) {
//                            System.out.println(c.getVehicleModel()+" added to scenegraph");
                            ACS.root.getChildren().add(1, c);
                        }
                        MoveTo sPoint = (MoveTo) ((Path) currentPathTransition.getPath()).getElements().get(0);
                        System.out.println("MoveTo X: "+sPoint.getX()+" Y: "+sPoint.getY());
                        if (sPoint.getX() == -6.330897808074951 && sPoint.getY() == 419.0694580078125 || 
                                sPoint.getX() == 37.5086555480957 && sPoint.getY() == 355.8201904296875 ) {
                                nextAnimation.setOnFinished(i -> {
                                    ACS.root.getChildren().remove(c);
                                    followUpAnimation.play();
                                        });
                        }else if(sPoint.getX() == -0.0010140599915757775 && sPoint.getY() == 418.9505615234375 ){
                            c.adjustRFIDLocation();
                            updateVisitorLog(c);
                            updateOccupancy(c.getAllocateSpace(), Car.occupanyStatus.PARKED);
                            parkedVehicles.add(c);
                        }else
                            c.adjustRFIDLocation();
                            

//                            if(sT.indexOf(nextAnimation) % 8 == 1){
//                                new Timer().schedule(new TimerTask(){
//                                    @Override
//                                    public void run(){
//                                        c.startTracking();
//                                    }
//                                }, 2000);
//                            }
//                            else
//                                new Timer().schedule(new TimerTask(){
//                                    @Override
//                                    public void run(){
//                                        c.endTracking();
////                                        c.startTracking();
//                                    }
//                                }, 0);

                        nextAnimation.play();
                        System.out.println("Playing Next Animation after ParallelTransition"+nextAnimation);

                    });
                }else if(nextAnimation instanceof PathTransition){
                        Car c = (Car)((PathTransition)nextAnimation).getNode();
                        boolean shouldReverse = ((currentIndex + 1) < sT.size()-1 && sT.get(currentIndex + 1) instanceof PathTransition);
                        a.setOnFinished(e -> {
                            if (!ACS.root.getChildren().contains(c)) {
                                ACS.root.getChildren().add(1, c);
                                c.adjustRFIDLocation();
                            } 

                            if(shouldReverse)
                                c.getCarIcon().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                            else{
                                c.getCarIcon().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                                c.adjustRFIDLocation();
                            }
                                
                            nextAnimation.play();
//                            System.out.println("Playing Next Animation after PathTransition"+nextAnimation);
                        });
                        
                }else{
                    a.setOnFinished(e -> {
                        nextAnimation.play();
//                        System.out.println("Playing Next Animation after PauseTransition or Timeline"+nextAnimation);
                            });
                }
                
            }else{
                a.setOnFinished(e -> printOccupancyArray());
                //output linegraph
            }
        }

//        Platform.runLater(() -> {
        sT.get(0).play();
        System.out.println("Animation Started");
//        });
    }
    
    public Timeline getBarrierAnimation(Node barrier, Duration d, int angle){
       Timeline t = new Timeline();
       t.getKeyFrames().add(new KeyFrame(d, new KeyValue(barrier.rotateProperty(), angle, Interpolator.EASE_OUT)));
       return t;
    }

    public ArrayList<Node> getControlSystem(){
        ImageView firstBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView secondBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView entExtBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView extExtBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView camera = new ImageView(new Image(getClass().getResourceAsStream("assets/Camera Sensor.png")));
        ImageView RFIDEntranceAuthenticator = new ImageView(new Image(getClass().getResourceAsStream("assets/RFID Sensor 2.png")));
        
        VACS_Components.addAll(Arrays.asList(firstBarrier, secondBarrier, entExtBarrier, extExtBarrier, camera, RFIDEntranceAuthenticator));
        
        return VACS_Components;
    }
    
    public void setControlSystemCoordinates(){
        for(Node n: VACS_Components){
            int index = VACS_Components.indexOf(n);
            switch(index){
                case 0:
                    n.setLayoutX(645);
                    n.setLayoutY(424);
                    break;
                case 1:
                    n.setLayoutX(645);
                    n.setLayoutY(320);
                    break;
                case 2:
                    n.setLayoutX(695);
                    n.setLayoutY(357);
                    n.setRotate(90);
                    break;
                case 3:
                    n.setLayoutX(52);
                    n.setLayoutY(321);
                    break;
                case 4:
                    n.setLayoutX(681);
                    n.setLayoutY(284);
                    ((ImageView)n).setFitHeight(25);
                    ((ImageView)n).setPreserveRatio(true);
                    break;
                case 5:
                    n.setLayoutX(680);
                    n.setLayoutY(284);
                    ((ImageView)n).setFitHeight(15);
                    ((ImageView)n).setPreserveRatio(true);
                    break;
            }
        }
    }
    
    private static String[] vehicleFileNames = {"Vehicle Coupe Grey", "Vehicle Limosin White", 
        "Vehicle Sedan Green", "Vehicle Truck RedWhite", "Vehicle Van Blue", "Vehicle Van LightBlue"};
    
    private static int[][] occupany = {
        {0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0}};
    
    private void printOccupancyArray(){
        for (int i = 0; i < occupany.length; i++) {
            for (int j = 0; j < occupany[i].length; j++) {
                System.out.print(occupany[i][j]+" ");
            }
            System.out.println("");
        }
    }
    
    public void resetOccupancy(){
        for (int i = 0; i < occupany.length; i++) {
            for (int j = 0; j < occupany[i].length; j++) {
               occupany[i][j] = 0;
            }
        }
    }
    
    public void generateParkedCars(int num){
        if(!parkedVehicles.isEmpty()){
            parkedVehicles.clear();
            resetOccupancy();
        }
        for (int i = 0; i < num; i++) {
            String allottedSpace = generatePS_Num();
            while(isOccupied(allottedSpace)){
                allottedSpace = generatePS_Num();
//                System.out.println("Generated Space: "+allottedSpace);
            }
//            System.out.println("Valid Generated Space: "+allottedSpace);
            
            int vehicleID = (int)(Math.random() * 6);
            Car car = new Car(vehicleFileNames[vehicleID]);
            car.setAllocateSpace(allottedSpace);
            updateOccupancy(allottedSpace, Car.occupanyStatus.PARKED);
//            System.out.println("Occupancy Updated");
            parkedVehicles.add(car);
        }
        
        printOccupancyArray();
    }
    
    public synchronized  void addCarToLayout(Pane pane, Car c, Point2D coord){
        pane.getChildren().add(c);
        c.setLayoutX(coord.getX());
        c.setLayoutY(coord.getY());
    }
    
    public void layoutParkedCars(Pane pane){
        for(Car c: parkedVehicles){
            String[] occupancyIndex = c.getAllocateSpace().split("[\\D]");
                    
                    int rowIndex = 0, columnIndex = 0;
                    switch (c.getAllocateSpace().charAt(0)) {
                        case 'A':
                            Platform.runLater(()-> {
                                double height = c.getCarIcon().getBoundsInLocal().getWidth();
                                c.setRotate(180);
                                c.setTranslateX((-height/2)+15);
                                c.setTranslateY(-13);
                                });
                            break;
                        case 'B':
                            rowIndex = 1;
                            Platform.runLater(()->{
                                c.setRotate(-90);
                                double height = c.getCarIcon().getBoundsInLocal().getWidth();
                                c.setTranslateX(-height/2);
                                c.setTranslateY(-15);
                            });
                            break;
                        case 'C':
                            rowIndex = 2;
                            Platform.runLater(()->{
                                c.setRotate(90);
                                c.getRFIDNumber().setRotate(-90);
                                double height = c.getCarIcon().getBoundsInLocal().getWidth();
                                c.setTranslateX(-height/2);
                                c.setTranslateY(-15);
                            });
                            break;
                        case 'D':
                            rowIndex = 3;
                            Platform.runLater(()->{
                                c.setRotate(90);
                                c.getRFIDNumber().setRotate(-90);
                                c.setTranslateY(-15);
                            });
                            break;
                        case 'E':
                            rowIndex = 4;
                            break;
                    }
                    columnIndex = Integer.parseInt(occupancyIndex[1]);

                    c.setLayoutX(parkingSpace[rowIndex][(columnIndex-1)].getX());
                    c.setLayoutY(parkingSpace[rowIndex][(columnIndex-1)].getY());
           
                    addCarToLayout(pane, c, parkingSpace[rowIndex][(columnIndex-1)]);
        }
    }
    
    private static Point2D[][] parkingSpace = {
        {new Point2D(700,249), new Point2D(700,209), new Point2D(700,167), new Point2D(700,128), new Point2D(700,78)},
        {new Point2D(578,281), new Point2D(536,281), new Point2D(491,281),new Point2D(456,281),new Point2D(413,281),new Point2D(368,281),new Point2D(324,281),new Point2D(279,281), new Point2D(241,281), new Point2D(199,281), new Point2D(157,281)},
        {new Point2D(583,150), new Point2D(542,150), new Point2D(500,150),new Point2D(456,151),new Point2D(413,150),new Point2D(368,150),new Point2D(324,150),new Point2D(279,150), new Point2D(241,150), new Point2D(199,150), new Point2D(157,150)},
        {new Point2D(550,29), new Point2D(509,29), new Point2D(467,29),new Point2D(423,29),new Point2D(380,29),new Point2D(348,29),new Point2D(291,29),new Point2D(246,29), new Point2D(208,29), new Point2D(166,29), new Point2D(124,29)},
        {new Point2D(4,235), new Point2D(4,195), new Point2D(4,155), new Point2D(4,115), new Point2D(4,68)}
    };
    
    private final static  ArrayList<Car> parkedVehicles = new ArrayList<>();

    public static ArrayList<Car> getParkedVehicles() {
        return parkedVehicles;
    }
    
    public void setNumOfCheckIn(int numOfVehicles){
        if(!checkInVehicles.isEmpty()){
            checkInVehicles.clear();
        } 
            
        int counter = 0;
        String toFirstBarrier = "entP_toFirstBarrier", toSecondBarrier = "entP_toSecondBarrier";
        while(counter < numOfVehicles){
            String allottedSpace = generatePS_Num();
            
            //if the allocated space is occupied, skip this loop and allocate another space
            if(isOccupied(allottedSpace))
                continue;
            String allottedSpacePathID = "entP_to"+allottedSpace;
//            System.out.println("Allotted Parking Space ["+counter+"]: "+allottedSpacePathID);
            int vehicleID = (int)(Math.random() * 6);
            Car car = new Car(vehicleFileNames[vehicleID]);
            car.setAllocateSpace(allottedSpace);
            checkInVehicles.add(car);
            setAnimationSequence(car, toFirstBarrier, toSecondBarrier, allottedSpacePathID);
            
            //set allocated space to occupied
//            updateOccupancy(allottedSpace, Car.occupanyStatus.PARKED);
            counter++;
        }
        
//        if(numOfVehicles != 0)
//            printOccupancyArray();
    }
    
    public void reconstructCheckInAnimation(){
        System.out.println("Reconstructing Animation for Vehicle Access Control");
        for(Car c: checkInVehicles){
            boolean isValid = false;
            for(AnimPlan_VisitorLog a: getVisitorLogTableData()){
                String RFID = a.getRFID();
                //confirms the vehicle from visitor logs and permitted time for parking on visitor log
                if(c.getRFIDNumber().getText().matches(RFID) && isWithinValidTimeAccess(a, c) || c.getAccessLevel() == Car.vehicleAccess.Residence){
                    System.out.println("Vehicle "+c.getRFIDNumber().getText()+" is granted Access"
                            +(c.getAccessLevel() == Car.vehicleAccess.Residence ? " because vehile is resident" : ""));
                    
                    isValid = true;
                    break;
                }
            }
            
            //get the index of the animation sequence for invalid vehicle access for animation reconstruction
            if(!isValid){
                ArrayList<Integer> animationIndices = new ArrayList<>();
                for (Animation animation : sT){
                    if (animation instanceof PathTransition) {
                        Path p = (Path) ((PathTransition) animation).getPath();
                        MoveTo sPoint = (MoveTo) p.getElements().get(0);

                        if (sPoint.getX() == -9.00101375579834 && sPoint.getY() == 418.95062255859375) 
                            animationIndices.add(sT.indexOf(animation));
                    }
                }

                //remove animation sequence of invalid vehicle access via index
                if(!sT.isEmpty()){
                    sT.removeIf((Animation t) -> (sT.indexOf(t) >= (animationIndices.get(0) - 1)) && (sT.indexOf(t) <= (animationIndices.get(0) + 5)));
                }

                setAnimationSequence(c, "entP_toFirstBarrier", "entP_toEntExt1", "entP_toEntExt2");
                
            }
        }
        
        int checkoutNum = (int)((ChoiceBox)cPanel.lookup("#cBoxCheckOut")).getValue();
        if(checkoutNum != 0){

            ArrayList<Integer> indices = new ArrayList<>();
           
            for(Animation a: sT)
                System.out.println("Reconstruction Before Checkout Reduction-> Animation: "+a);
        
            for(Animation a: sT){
                int index = sT.indexOf(a);
                if(a instanceof PathTransition && sT.get(index + 1) instanceof PathTransition ||
                        a instanceof PathTransition && sT.get(index + 3) instanceof PauseTransition && sT.get(index - 1) instanceof PauseTransition){
                    indices.add(index);
                }
            }
            
            for(int i = (indices.size() - 1); i >= 0; i--){
                int index = indices.get(i);
                if(sT.get(index + 3) instanceof PauseTransition)
                    sT.removeIf((Animation a) -> (sT.indexOf(a) >= (index - 1) && sT.indexOf(a) <= (index + 3)));
                else
                    sT.removeIf((Animation a) -> (sT.indexOf(a) >= (index - 1) && sT.indexOf(a) < (index + 5)));
            }
//            
            for(Animation a: sT)
                System.out.println("Reconstruction After Checkout Reduction-> Animation: "+a);
            
            setNumOfCheckOut(checkoutNum, true);
//            for(Animation a: sT)
//                System.out.println("Reconstruction After-> Animation: "+a);
        }
    }
    
    public void updateVisitorLog(Car c){
        TableView<AnimPlan_VisitorLog> table = (TableView) ACS.root.lookup("#tableView");
        for(AnimPlan_VisitorLog a: table.getItems()){
            if(a.getRFID().matches(c.getRFIDNumber().getText())){
                a.setStatus("Packed");
                System.out.println("Visitor Log updated");
                table.refresh();
                
            }
        }
    }
    
    public boolean isWithinValidTimeAccess(AnimPlan_VisitorLog a, Car c){
        System.out.println("Current Time-In is: "+a.getActivity_TimeIn()+" & Time-Out: "+a.getTime_TimeOut());
        LocalTime vLTimeIn = LocalTime.of(
                (Integer.parseInt(a.getActivity_TimeIn().substring(0, 2)) == 24 ? 23 : Integer.parseInt(a.getActivity_TimeIn().substring(0, 2))), 
                (Integer.parseInt(a.getActivity_TimeIn().substring(3, 5)) == 60 ? 59: Integer.parseInt(a.getActivity_TimeIn().substring(3, 5))), 
                (Integer.parseInt(a.getActivity_TimeIn().substring(6, 8)) == 60 ? 59: Integer.parseInt(a.getActivity_TimeIn().substring(6, 8))));
        
        LocalTime vLTimeOut = LocalTime.of(
                (Integer.parseInt(a.getTime_TimeOut().substring(0, 2)) == 24 ? 23 : Integer.parseInt(a.getTime_TimeOut().substring(0, 2))), 
                (Integer.parseInt(a.getTime_TimeOut().substring(3, 5)) == 60 ? 59 : Integer.parseInt(a.getTime_TimeOut().substring(3, 5))), 
                (Integer.parseInt(a.getTime_TimeOut().substring(6, 8)) == 60 ? 59 : Integer.parseInt(a.getTime_TimeOut().substring(6, 8))));
        
        ArrayList<Integer> animationTimes = getAnimationTimes(c);
        
        int animStartTime = animationTimes.get(0);
        int animationDuration = animationTimes.get(1);
        
        String animationStartTime = getTime(0, animStartTime);
        String animationEndTime = getTime(0, animStartTime+animationDuration);
        System.out.println("Animation Time-In is: "+animationStartTime+" & Time-Out: "+animationEndTime);
        LocalTime aSTime = LocalTime.of(
                (Integer.parseInt(animationStartTime.substring(0, 2)) == 24 ? 23 : Integer.parseInt(animationStartTime.substring(0, 2))), 
                (Integer.parseInt(animationStartTime.substring(3, 5)) == 60 ? 59 : Integer.parseInt(animationStartTime.substring(3, 5))), 
                (Integer.parseInt(animationStartTime.substring(6, 8)) == 60 ? 59 : Integer.parseInt(animationStartTime.substring(6, 8))));
        LocalTime aETime = LocalTime.of(
                (Integer.parseInt(animationEndTime.substring(0, 2)) == 24 ? 23 : Integer.parseInt(animationEndTime.substring(0, 2))), 
                (Integer.parseInt(animationEndTime.substring(3, 5)) == 60 ? 59 : Integer.parseInt(animationEndTime.substring(3, 5))), 
                (Integer.parseInt(animationEndTime.substring(6, 8)) == 60 ? 59 : Integer.parseInt(animationEndTime.substring(6, 8))));
        
        return vLTimeIn.isBefore(aSTime) && vLTimeOut.isAfter(aETime);
    }
    
    public ArrayList<Integer> getAnimationTimes(Car c){
        ArrayList<Integer> animationTimes = new ArrayList<>();
        int animationDuration = 0, animStartTime = 0, index = 0;
        
        for (Animation animation : sT) {
            if (index != 0 && animation instanceof ParallelTransition
                    && !((PathTransition) ((ParallelTransition) animation).getChildren().get(1)).getNode().equals(c) && !(animation instanceof Timeline)) {
                break;
            } else if (animation instanceof ParallelTransition) {
                PathTransition pT = (PathTransition) ((ParallelTransition) animation).getChildren().get(1);
                MoveTo sPoint = (MoveTo) ((Path) pT.getPath()).getElements().get(0);
                if (sPoint.getX() == -0.0010140599915757775 && sPoint.getY() == 418.9505615234375) {
                    animationDuration += (5 / pT.getRate());
                }

            } else if (animation instanceof PathTransition && ((PathTransition) animation).getNode().equals(c)) {
                    index = sT.indexOf(animation);
                    animStartTime += animationDuration + ((PathTransition) animation).getDuration().toSeconds();
            }
        }
        animationTimes.add(animStartTime);
        animationTimes.add(animationDuration);
        
        return animationTimes;
    }

    public static ArrayList<Car> getCheckInVehicles() {
        return checkInVehicles;
    }
    
    private final static ArrayList<Car> checkInVehicles = new ArrayList<>();
    
    private Point2D[][] exitPathTranslations = {{new Point2D(-708,-210), new Point2D(-708,-200), new Point2D(-708,-140), new Point2D(-708,-100),new Point2D(-708,-60)},
        {new Point2D(-585,-274), new Point2D(-540,-274), new Point2D(-498,-274), new Point2D(-460,-274), new Point2D(-420,-274), new Point2D(-375,-274), new Point2D(-330,-274), new Point2D(-288,-274), new Point2D(-246,-272), new Point2D(-206,-274), new Point2D(-161,-270)},
        {new Point2D(-590,-140), new Point2D(-540,-130), new Point2D(-498,-130), new Point2D(-460,-130), new Point2D(-420,-130), new Point2D(-375,-130), new Point2D(-330,-130), new Point2D(-285,-130), new Point2D(-246,-130), new Point2D(-206,-130), new Point2D(-161,-130)},
        {new Point2D(-520,-20), new Point2D(-520,-20), new Point2D(-468,-20), new Point2D(-430,-20), new Point2D(-390,-20), new Point2D(-345,-20), new Point2D(-300,-20), new Point2D(-255,-20), new Point2D(-215,-20), new Point2D(-176,-20), new Point2D(-131,-20)},
        {new Point2D(-13,-235), new Point2D(-13,-190), new Point2D(-13,-150), new Point2D(-13,-105), new Point2D(-13,-60)}};
    
    private Point2D getExitPathTranslations(String allocatedSpace){
        char rowCharIndex = allocatedSpace.charAt(0);
        int columnIndex = Integer.parseInt(allocatedSpace.substring(1));
        int rowIndex = 0;

        switch (rowCharIndex) {
            case 'B':
                rowIndex = 1;
                break;
            case 'C':
                rowIndex = 2;
                break;
            case 'D':
                rowIndex = 3;
                break;
            case 'E':
                rowIndex = 4;
                break;
        }
        
        return exitPathTranslations[rowIndex][--columnIndex];
    }
    
    public final String generatePS_Num(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 2; i++){
            int charCode = '\u0000';
            if(i % 2 == 0){
                charCode = 65 + (int)(Math.random() * 5);
//                charCode = 67 + (int)(Math.random() * 3);//charCode for C - E
                builder.append(Character.toChars(charCode)[0]);
            }else{
                //if the generated section is A or E, generate a random space between the available space 1 - 5 in section A or E, 
                //else generate a number between 1 - 11 for available spance in section B, C or D
                int numCode =  1 + (int)(Math.random() * (builder.charAt(0) == 'A'|| builder.charAt(0) == 'E'? 5 : 11));
//                System.out.println("Generated space number: "+numCode+" for generated character "+ builder.charAt(0));
                builder.append(numCode);
            }
        }
        return builder.toString();
    }
    
    private synchronized void updateOccupancy(String allottedSpace, Car.occupanyStatus status){
        String[] occupancyIndex = allottedSpace.split("[\\D]");
        int rowIndex = 0, columnIndex = 0;
        switch(allottedSpace.charAt(0)){
            case 'B':
                rowIndex = 1;
                break;
            case 'C':
                rowIndex = 2;
                break;
            case 'D':
                rowIndex = 3;
                break;
            case 'E':
                rowIndex = 4;
                break;
        }
        columnIndex = Integer.parseInt(occupancyIndex[1]);
        
        if(status == Car.occupanyStatus.PARKED)
            occupany[rowIndex][--columnIndex] = 1;
        else
            occupany[rowIndex][--columnIndex] = 0;
//        return 
     }
    
    private boolean isOccupied(String allottedSpace){
        String[] occupancyIndex = allottedSpace.split("[\\D]");
        int rowIndex = 0;
        switch(allottedSpace.charAt(0)){
            case 'B':
                rowIndex = 1;
                break;
            case 'C':
                rowIndex = 2;
                break;
            case 'D':
                rowIndex = 3;
                break;
            case 'E':
                rowIndex = 4;
                break;
        }
        int columnIndex = Integer.parseInt(occupancyIndex[1]);
//        System.out.println("Row Index: "+rowIndex+", Column Index: "+(columnIndex-1));
        
        return (occupany[rowIndex][--columnIndex] == 1);
    }
    
    Animation curAnimation;
    
    public void pauseAnimation(){
        for (Animation a : sT) {
            if (a.getStatus() == Animation.Status.RUNNING) {
                curAnimation = a;
                a.pause();
            }
        }
    }
    
    public void playAnimation(){
        if(curAnimation != null)
            curAnimation.play();
        else
            playAnimationSequence();
    }
    
    public void setNumOfCheckOut(int numOfVehicles, boolean reconstruct){
        int counter = 0;
        String extP_toExtExt = "extP_toExtExt";
        while (counter < numOfVehicles) {
            String allottedSpace = getOccupiedParkingLot();
            System.out.println("Generated parking space: "+allottedSpace);

            String toExitPath = "extP_toExtFrom"+allottedSpace+(allottedSpace.matches("C[\\d]+") ? "":"Reverse");
            String reversePath = "extP_reverseFrom"+allottedSpace;
            System.out.println("Vehicle is exiting from Allotted Parking Space [" + counter + "]: " + allottedSpace);
            Car checkoutCar = getCarInAllotedSpace(allottedSpace);
            if(!reversePath.matches("extP_[\\w]{1,}C[\\d]+"))
                setAnimationSequence(checkoutCar, reversePath, toExitPath, extP_toExtExt);
            else
                setAnimationSequence(checkoutCar, toExitPath, extP_toExtExt);

            //set allocated space to occupied
            if(reconstruct)
                updateOccupancy(allottedSpace, Car.occupanyStatus.UNPARKED);
            counter++;
        }
    }
    
    public synchronized Car getCarInAllotedSpace(String allottedSpace){
        Car car = new Car("Vehicle Sedan Green");
//        System.out.println("Default car is Vehicle Sedan Green");
        for(Car c: parkedVehicles){
            System.out.println("Parked Car "+c.getVehicleModel()+" @ "+c.getAllocateSpace());
            if(c.getAllocateSpace().equals(allottedSpace)){
                car = c;
                System.out.println("Retreived car is "+c.getVehicleModel()+" from parking lot "+c.getAllocateSpace());
                break;
            }
        }
        return car;
    }
    
    public String getOccupiedParkingLot(){
        String output = ""; boolean found = false;
        for (int i = 0; i < occupany.length; i++) {
            if(found){
               System.out.println("goal found, breaking loop");
               break; 
            }
            for (int j = 0; j < occupany[i].length; j++) {
//                System.out.println("Accessing Occupancy Array @ row: "+i+", column: "+j);
                if(occupany[i][j] == 1){
                    System.out.println("Vehicle found @ Parking Space ["+i+"]["+j+"]");
                    found = true;
                    switch(i){
                        case 0:
                            output += "A";
                            break;
                        case 1:
                            output += "B";
                            break;
                        case 2:
                            output += "C";
                            break;
                        case 3:
                            output += "D";
                            break;
                        case 4:
                            output += "E";
                            break;
                    }
                    int parkinglotNumber = ++j;
                    output += parkinglotNumber;
                    break;
                }
            }
        }
        
        return output;
    }
        
    private final ObservableList<AnimPlan_VisitorLog> visitorLogTableData = FXCollections.observableArrayList();
    private final ObservableList<AnimPlan_VisitorLog> animSequenceTableData = FXCollections.observableArrayList();
    
    public ObservableList<AnimPlan_VisitorLog> getVisitorLogTableData(){
        return visitorLogTableData;
    }
    
    public ObservableList<AnimPlan_VisitorLog> getAnimSequenceTableData(){
        return animSequenceTableData;
    }
    
    public void getAnimSequenceData(){
         if(!animSequenceTableData.isEmpty())
            animSequenceTableData.clear();
        int count = 0;
        for(Animation a: sT){
            if(a instanceof PathTransition){
                Path p = (Path)((PathTransition)a).getPath();
                MoveTo sPoint = (MoveTo)p.getElements().get(0);
                Car c = (Car)((PathTransition)a).getNode();
               
                if(sPoint.getX() == -9.00101375579834 && sPoint.getY() == 418.95062255859375)
                    animSequenceTableData.add(new AnimPlan_VisitorLog(++count, c.getRFIDNumber().getText(), "Check-In", 
                            getTime(0, getAnimationTimes(c).get(0)), "Waiting"));
                
            }else if(a instanceof ParallelTransition){
                PathTransition pT = (PathTransition)((ParallelTransition)a).getChildren().get(1);
                Path p = (Path)pT.getPath();
                MoveTo sPoint = (MoveTo)p.getElements().get(0);
                Car c = (Car)pT.getNode();
                
                if(sPoint.getX() == -6.330897808074951 && sPoint.getY() == 419.0694580078125)
                    animSequenceTableData.add(new AnimPlan_VisitorLog(++count, c.getRFIDNumber().getText(), "Check-Out", 
                            getTime(0, getAnimationTimes(c).get(0)), "Waiting"));
            }
        }
    }
    
    public void generateVisitorLog(){
        //clear visitor log table before generating new data
        if(!visitorLogTableData.isEmpty())
            visitorLogTableData.clear();
        
        //generate random values for the table
        for (int i = 0; i < 5; i++) {
            int rand = (int)(Math.random() * 5);
            visitorLogTableData.add(new AnimPlan_VisitorLog((i+1), new Car().getRFIDNumber().getText(), getTime(rand-2, 0), getTime(rand+5, 0), "Unpacked"));
        }
        
        int[] valOccurrence = {0,0,0,0,0};
        int checkedCarCount = 0;
        
        //set the visitor log data to randomly include generated vehicle RFID numbers
        for(int i = 0; i < valOccurrence.length; i++){
            
            if(checkedCarCount < checkInVehicles.size()){
                valOccurrence[i] = (int) (Math.random() * 2);
                
                if(valOccurrence[i] == 1)
                    visitorLogTableData.get(i).setRFID(checkInVehicles.get(checkedCarCount++).getRFIDNumber().getText());
            }
            else
                break;
        }

    }
        
    public void setToggle(ActionEvent e){
        TableView tableView = (TableView) ACS.cPanel.lookup("#tableView");
        
        if(!tableView.getItems().isEmpty())
            tableView.getItems().clear();
        
        TableColumn col1 = (TableColumn) tableView.getColumns().get(2);
        TableColumn col2 = (TableColumn) tableView.getColumns().get(3);
        
        ToggleButton tgBtnAnimSequence = (ToggleButton) cPanel.lookup("#tgBtnAnimSequence");

        if ((e.getSource()).equals(tgBtnAnimSequence)) {
            tableView.refresh();
            col1.setText("ACTIVITY");
            col2.setText("TIME");
            tableView.getItems().addAll(getAnimSequenceTableData());
        } else {
            col1.setText("TIME-IN");
            col2.setText("TIME-OUT");
            tableView.getItems().addAll(getVisitorLogTableData());
        }
    }
    
    private String getTime(int minExtension, int secExtension){
        long currentTime = System.currentTimeMillis();
        int seconds = (int) (currentTime/1000) % 60;
        int minutes = (int)(currentTime / 60000) % 60;
        int hours = (int)(currentTime / 3600000) % 24;
        
        if((secExtension + seconds) > 60 ){
            seconds = (secExtension + seconds) % 60;
            minutes++;
        }else if((minExtension + minutes) > 60){
            minutes = (minExtension + minutes) % 60;
            hours++;
        }
        else{
            seconds += secExtension;
            minutes += minExtension;
        }
        
        return (++hours < 10 ? "0"+hours : hours)+":"+(minutes < 10 ? "0"+minutes : minutes)+":"+(seconds < 10 ? "0"+seconds : seconds);
    }
    
    public static class AnimPlan_VisitorLog{
        private final SimpleIntegerProperty Index;
        private final SimpleStringProperty RFID;
        private final SimpleStringProperty Activity_TimeIn;
        private final SimpleStringProperty Time_TimeOut;
        private final SimpleStringProperty Status;

        public AnimPlan_VisitorLog(int Index, String RFID, String Activity_TimeIn, String Time_TimeOut, String Status) {
            this.Index = new SimpleIntegerProperty(Index);
            this.RFID = new SimpleStringProperty(RFID);
            this.Activity_TimeIn = new SimpleStringProperty(Activity_TimeIn);
            this.Status = new SimpleStringProperty(Status);
            this.Time_TimeOut = new SimpleStringProperty(Time_TimeOut);
        }

        public SimpleIntegerProperty indexProperty(){
            return Index;
        }

        public SimpleStringProperty rfidProperty(){
            return RFID;
        }

        public SimpleStringProperty activity_TimeInProperty(){
            return Activity_TimeIn;
        }
        
        public SimpleStringProperty time_TimeOutProperty(){
            return Time_TimeOut;
        }
        
        public SimpleStringProperty statusProperty(){
            return Status;
        }
        
        public int getIndex() {
            return Index.get();
        }

        public String getRFID() {
            return RFID.get();
        }

        public String getActivity_TimeIn() {
            return Activity_TimeIn.get();
        }
        
        public String getTime_TimeOut() {
            return Time_TimeOut.get();
        }

        public String getStatus() {
            return Status.get();
        }
        
        public void setIndex(int index){
            Index.set(index);
        }
        
        public void setRFID(String RFID){
            this.RFID.set(RFID);
        }
        
        public void setActivity_TimeIn(String Activity_TimeIn){
            this.Activity_TimeIn.set(Activity_TimeIn);
        }
        
        public void setStatus(String status){
            Status.set(status);
        }

        public void setTime_TimeOut(String Time_TimeOut){
            this.Time_TimeOut.set(Time_TimeOut);
        }
    }
    
}