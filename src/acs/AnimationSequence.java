/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
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
//    private SequentialTransition sT;
    private SVGDocument svgDrivePath;
    private static double[][] animationRate = {
        {-1,-.95,-.85,.75,.65}, 
        {-1,-1,-.95,-.95,-.85,-.85,-.75,-.75,-.65,-.65,-.5}, 
        {-1,-1,-.95,-.95,-.85,-.85,-.75,-.75,-.65,-.65,-.5}, 
        {.85,.85,.85,.75,.75,.75,.65,.65,.65,.5,.5}, 
        {-.5,-.5,-.5,-.5,-.5},
        {-1,-1,-1,1,1},
        {.5, .5, -.5, .5, .5},
        {.5, .5, .65, .65, .65, .75, .75, .75, .85, .85, .85},
        {.5, .65, .65, .75, .75, .85, .85, .95, .95, 1, 1},
        {-.5, -.65, -.65, -.75, -.75, -.85, -.85, -.95, -.95, -1, -1},
        {1, .95, .85, .75, .65},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1}};
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

            File drivePath = new File("src/acs/assets/FloorPlanDrivePath.svg");
            svgDrivePath = f.createSVGDocument(drivePath.getAbsoluteFile().toURI().toString());
        }catch(IOException ex){}
        
    }
    
    public Path getPath(String pathID){
        PathParser parser = new PathParser();
        JavaFXPathElementHandler handler = new JavaFXPathElementHandler();
        parser.setPathHandler(handler);
        String pathData, matrixTransform;

//        SAXParser p = new SAXParser();
        Element selectedPath = svgDrivePath.getElementById(pathID);
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
                System.out.println("Column Index: "+columnIndex);
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
                System.out.println("Row Index: "+rowIndex);

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
        }else if(pathID.matches("extP_toExtFrom[\\S]{9,}")){
                output = pathID.substring(14);
                c = output.charAt(0);
                rowIndex = 6;
                String columnIndexString = pathID.substring(15, 17);
               
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
                System.out.println("extracted character @ index 16 is "+c);
                columnIndex = Integer.parseInt(pathID.substring(17));
                System.out.println("extracted character between index 15 and 17 is "+(pathID.length() == 18 ? pathID.substring(18) : pathID.substring(18)));
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
                System.out.println("Animation Rate for Row: "+rowIndex+" & Column: "+(columnIndex - 1));
                return animationRate[rowIndex][columnIndex - 1];
        }else
            return 1;
               
    }

    public final void setAnimationSequence(Node car, String... pathID) {
        ArrayList<Animation> tempAnimationList = new ArrayList<>();
        for(int i = 0; i < pathID.length; i++){
            System.out.println("Get animation path for svg path id: "+pathID[i]);
            Path p = getPath(pathID[i]);
            if(pathID[i].matches("extP_toExtFrom[\\S]{9,}") || pathID[i].matches("extP_[\\w]{1,}C")){
                p.setTranslateX(-500);
                p.setTranslateY(-500);
            }

            p.setTranslateY(8);
            PathTransition pT = new PathTransition(Duration.seconds(5), p, car);
            pT.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pT.setInterpolator(Interpolator.EASE_BOTH);
            pT.setRate(getAnimRateFromPathID(pathID[i]));
            System.out.println("Animation Rate ["+i+"]: "+pT.getRate());
            
            if(pathID[i].matches("extP_reverseFrom[\\S]{2,}")){
                Platform.runLater(()->{
                    ((Car)car).getCarIcon().setNodeOrientation(pT.getRate() > 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
                    car.setNodeOrientation(pT.getRate() > 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
                });
            }
//            else
//            car.setRotate(pT.getRate() == 1 ? 0 : 180);
            Platform.runLater(()->{
                System.out.println("Path "+pT+" has animation rate "+pT.getRate());
                ((Car)car).getCarIcon().setNodeOrientation(pT.getRate() < 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
                System.out.println("Assigned Node Orientation "+((Car)car).getCarIcon().getNodeOrientation());
            });
//          car.setNodeOrientation(pT.getRate() <= 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
                
            tempAnimationList.add(pT);
            
            ChoiceBox c = ((ChoiceBox)ACS.root.lookup("#cBoxTimer"));
            
//            System.out.println("ChoiceBox value: "+c.getValue());
//            System.out.println("ChoiceBox value: "+c);
            int barrierDuration = Integer.parseInt((c.getValue().toString()));
//            int barrierDuration = 3;
            
            int counter = 0;
            counter = tempAnimationList.stream().filter((a) -> (a instanceof Timeline)).map((_item) -> 1).reduce(counter, Integer::sum);
            if (pathID[i].matches("entP_toFirstBarrier")) {
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(0), Duration.seconds(barrierDuration), -45));
//                ((Car) car).getCarIcon().setNodeOrientation(pT.getRate() == 1 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
            }else if(pathID[i].matches("entP_toSecondBarrier")){
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(0), Duration.seconds(barrierDuration), 0));
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(1), Duration.seconds(barrierDuration), -45));
            }else if(pathID[i].matches("entP_to[\\S]{2,3}") && (counter == 3)){
                    tempAnimationList.add(getBarrierAnimation(VACS_Components.get(1), Duration.seconds(barrierDuration), 0));
            }else if(pathID[i].matches("extP_toExtFrom[\\S]{9,}") && (counter == 4)){
                tempAnimationList.add(sT.size() - 2, getBarrierAnimation(VACS_Components.get(3), Duration.seconds(barrierDuration), -45));
                tempAnimationList.add(getBarrierAnimation(VACS_Components.get(3), Duration.seconds(barrierDuration), 0));
            }
        }

        tempAnimationList.add(0, new PauseTransition(Duration.seconds(2)));
        
        sT.addAll(tempAnimationList);
//        sTClone.addAll(tempAnimationList);s
        System.out.println("Animation contains: "+sT.size());
    }

    public ArrayList<Animation> getAnimationSequence() {
        return sT;
    }

    
    public void playAnimationSequence(){
        
        for(Animation a: sT){
            System.out.println("Animation "+a);
            int currentIndex = sT.indexOf(a);
                if(currentIndex < (sT.size()-1)){
                    Animation nextAnimation = sT.get(++currentIndex);
                    if(nextAnimation instanceof PathTransition){
                        Car c = (Car) ((PathTransition)nextAnimation).getNode();
                        a.setOnFinished(e-> Platform.runLater(()-> {

                            if (!ACS.root.getChildren().contains(c)) {
                                c.getCarIcon().setNodeOrientation(nextAnimation.getRate() < 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
                                System.out.println(c.getVehicleModel()+" added to scenegraph");
                                ACS.root.getChildren().add(c);
                            }else
                                c.getCarIcon().setNodeOrientation(nextAnimation.getRate() < 0 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);

                            System.out.println("Location of "+c+" is X: "+c.getTranslateX()+", Y: "+c.getTranslateY());
                            System.out.println("Node Orientation in PT "+((PathTransition)nextAnimation)+" of "+((PathTransition)nextAnimation).getNode()+" Car: "+((PathTransition)nextAnimation).getNode().getNodeOrientation()+" Transition Rate: "+((PathTransition)nextAnimation).getRate());
                            System.out.println("Node Orientation in PT "+((PathTransition)nextAnimation)+" of "+((Car)((PathTransition)nextAnimation).getNode()).getCarIcon()+"Car Icon: "+((Car)((PathTransition)nextAnimation).getNode()).getCarIcon().getNodeOrientation()+" Transition Rate: "+((PathTransition)nextAnimation).getRate());
                            
                            System.out.println("Modulus: "+sT.indexOf(nextAnimation) % 8);
                            if(sT.indexOf(nextAnimation) % 8 == 1){
                                new Timer().schedule(new TimerTask(){
                                    @Override
                                    public void run(){
                                        c.startTracking();
                                    }
                                }, 2000);
                            }
                            else
                                new Timer().schedule(new TimerTask(){
                                    @Override
                                    public void run(){
                                        c.endTracking();
//                                        c.startTracking();
                                    }
                                }, 0);
                            
                            nextAnimation.play();
                            System.out.println("Playing Next Animation");
                            
                        }));
//                        int index = currentIndex / 8;
//                        System.out.println("Pause Transition Detected @ index "+index);
//                        Platform.runLater(()->{
//                            if (!ACS.root.getChildren().contains(checkInVehicles.get(index))) {
//                                    ACS.root.getChildren().add(checkInVehicles.get(index));
//                                    System.out.println(checkInVehicles.get(index).getVehicleModel()+" added to scenegraph");
//                                }
//                        });
                    }else
                        a.setOnFinished(e -> nextAnimation.play());
                }else
                    a.setOnFinished(e -> printOccupancyArray());
        }

        Platform.runLater(() -> {
            sT.get(0).play();
            System.out.println("Animation Started");
        });
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
                    n.setLayoutY(404);
                    break;
                case 1:
                    n.setLayoutX(645);
                    n.setLayoutY(300);
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
    
    private static String[] vehicleFileNames = {"Vehicle Coupe Grey", "Vehicle Limosin White", "Vehicle Sedan Green", "Vehicle Truck RedWhite", "Vehicle Van Blue", "Vehicle Van LightBlue"};
    
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
        if(!parkedVehicles.isEmpty())
            parkedVehicles.clear();
        for (int i = 0; i < num; i++) {
            String allottedSpace = generatePS_Num();
            while(isOccupied(allottedSpace)){
                allottedSpace = generatePS_Num();
                System.out.println("Generated Space: "+allottedSpace);
            }
            System.out.println("Valid Generated Space: "+allottedSpace);
            
            int vehicleID = (int)(Math.random() * 6);
            Car car = new Car(vehicleFileNames[vehicleID]);
            car.setAllocateSpace(allottedSpace);
            updateOccupancy(allottedSpace, Car.occupanyStatus.PARKED);
            System.out.println("Occupancy Updated");
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
                            c.setRotate(180);
                        case 'B':
                            rowIndex = 1;
                            c.setRotate(-90);
                            break;
                        case 'C':
                            rowIndex = 2;
                            c.setRotate(90);
                            c.getPlateNumber().setRotate(-90);
                            break;
                        case 'D':
                            c.setRotate(90);
                            c.getPlateNumber().setRotate(-90);
                            rowIndex = 3;
                            break;
                        case 'E':
                            rowIndex = 4;
                            break;
                    }
                    columnIndex = Integer.parseInt(occupancyIndex[1]);

                    c.setLayoutX(parkingSpace[rowIndex][(columnIndex-1)].getX());
                    c.setLayoutY(parkingSpace[rowIndex][(columnIndex-1)].getY());
           
                    addCarToLayout(pane, c, parkingSpace[rowIndex][(columnIndex-1)]);
//                    pane.getChildren().add(c);
                    System.out.println(c.getVehicleModel()+" layoutX: "+c.getLayoutX()+"LayoutY: "+c.getLayoutY());
//                }
//            }
        }
    }
    
    private static Point2D[][] parkingSpace = {
        {new Point2D(700,249), new Point2D(700,209), new Point2D(700,167), new Point2D(700,128), new Point2D(700,78)},
        {new Point2D(578,281), new Point2D(536,281), new Point2D(491,281),new Point2D(456,281),new Point2D(413,281),new Point2D(368,281),new Point2D(324,281),new Point2D(279,281), new Point2D(241,281), new Point2D(199,281), new Point2D(157,281)},
        {new Point2D(583,150), new Point2D(542,150), new Point2D(500,150),new Point2D(456,151),new Point2D(413,150),new Point2D(368,150),new Point2D(324,150),new Point2D(279,150), new Point2D(241,150), new Point2D(199,150), new Point2D(157,150)},
        {new Point2D(550,29), new Point2D(509,29), new Point2D(467,29),new Point2D(423,29),new Point2D(380,29),new Point2D(348,29),new Point2D(291,29),new Point2D(246,29), new Point2D(208,29), new Point2D(166,29), new Point2D(124,29)},
        {new Point2D(4,235), new Point2D(4,195), new Point2D(4,155), new Point2D(4,115), new Point2D(4,68)}
    };
    
    private final static ArrayList<Car> parkedVehicles = new ArrayList<>();

    public static ArrayList<Car> getParkedVehicles() {
        return parkedVehicles;
    }
    
    public void setNumOfCheckIn(int numOfVehicles){
        if (!checkInVehicles.isEmpty()){
            checkInVehicles.clear();
            sT.clear();
            resetOccupancy();
        } 
        int counter = 0;
        String toFirstBarrier = "entP_toFirstBarrier", toSecondBarrier = "entP_toSecondBarrier";
        while(counter < numOfVehicles){
            String allottedSpace = generatePS_Num();
            
            //if the allocated space is occupied, skip this loop and allocate another space
            if(isOccupied(allottedSpace))
                continue;
            String allottedSpacePathID = "entP_to"+allottedSpace;
            System.out.println("Allotted Parking Space ["+counter+"]: "+allottedSpacePathID);
            int vehicleID = (int)(Math.random() * 6);
            Car car = new Car(vehicleFileNames[vehicleID]);
            car.setAllocateSpace(allottedSpace);
            checkInVehicles.add(car);
            setAnimationSequence(car, toFirstBarrier, toSecondBarrier, allottedSpacePathID);
            
            //set allocated space to occupied
            updateOccupancy(allottedSpace, Car.occupanyStatus.PARKED);
            counter++;
        }
    }

    public static ArrayList<Car> getCheckInVehicles() {
        return checkInVehicles;
    }
    
    private final static ArrayList<Car> checkInVehicles = new ArrayList<>();
    
    public final String generatePS_Num(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 2; i++){
            int charCode = '\u0000';
            if(i % 2 == 0){
                charCode = 65 + (int)(Math.random() * 5);
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
        System.out.println("Row Index: "+rowIndex+", Column Index: "+(columnIndex-1));
        
        return (occupany[rowIndex][--columnIndex] == 1);
    }
    
    
    public void setNumOfCheckOut(int numOfVehicles){
        if(!sT.isEmpty())
            sT.clear();
        int counter = 0;
        String extP_toExtExt = "extP_toExtExt";
        while (counter < numOfVehicles) {
            String allottedSpace = getOccupiedParkingLot();
            System.out.println("Generated parking space: "+allottedSpace);
//            
//            //if the allocated space is occupied, skip this loop and allocate another space
//            if (isOccupied(allottedSpace)) {
//                continue;
//            }
            String toExitPath = "extP_toExtFrom"+allottedSpace+"Reverse";
            String reversePath = "extP_reverseFrom"+allottedSpace;
            System.out.println("Vehicle is exiting from Allotted Parking Space [" + counter + "]: " + allottedSpace);
            Car checkoutCar = getCarInAllotedSpace(allottedSpace);
            if(!allottedSpace.matches("extP_[\\w]{1,}C"))
                setAnimationSequence(checkoutCar, reversePath, toExitPath, extP_toExtExt);
            else
                setAnimationSequence(checkoutCar, toExitPath, extP_toExtExt);

            //set allocated space to occupied
            updateOccupancy(allottedSpace, Car.occupanyStatus.UNPARKED);
            counter++;
        }
    }
    
    public Car getCarInAllotedSpace(String allottedSpace){
        Car car = new Car("Vehicle Sedan Green");
        System.out.println("Default car is Vehicle Sedan Green");
        for(Car c: parkedVehicles){
            System.out.println("Parked Car "+c.getVehicleModel()+" @ "+c.getAllocateSpace());
            if(c.getAllocateSpace().equals(allottedSpace)){
                car = c;
                System.out.println("Retreived car is "+c.getVehicleModel());
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
                if(occupany[j][i] == 1){
                    System.out.println("Vehicle found @ Parking Space ["+j+"]["+i+"]");
                    found = true;
                    switch(j){
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
                    int parkinglotNumber = ++i;
                    output += parkinglotNumber;
                    break;
                }
            }
        }
        
        return output;
    }
}