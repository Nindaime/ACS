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
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
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
    private static int[][] animationRate = {
        {-1,1,-1,1,1}, 
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}, 
        {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}, 
        {1,1,1,1,1,1,1,1,1,1,1}, 
        {-1,-1,-1,-1,-1},
        {-1,-1,-1,1,1},
        {-1, 1, -1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
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
        String pathData = "", matrixTransform = "";

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
    
    public int getAnimRateFromPathID(String pathID){
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
                    case "entP_toExtExt1":
                        columnIndex = 3;
                        break;
                    case "extP_toExtExt2":
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
                output = pathID.substring(15);
                c = output.charAt(0);
                rowIndex = 11;
//                pathID.s
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
                return animationRate[rowIndex][--columnIndex];
        }else
            return 1;
               
    }

    public final void setAnimationSequence(Node car, String... pathID) {

        for(int i = 0; i < pathID.length; i++){
            Path p = getPath(pathID[i]);
            if(pathID[i].matches("extP_toExtFrom[\\S]{9,}"))
                p.setTranslateX(15);
            p.setTranslateY(8);
            PathTransition pT = new PathTransition(Duration.seconds(5), p, car);
            pT.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pT.setInterpolator(Interpolator.EASE_BOTH);
            pT.setRate(getAnimRateFromPathID(pathID[i]));
            System.out.println("Animation ["+i+"]: "+pT.getRate());
            
//            if(pathID[i].matches("extP_reverseFrom[\\S]{2,}")){
//                car.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
//            }
//            else
//            car.setRotate(pT.getRate() == 1 ? 0 : 180);
            ((Car)car).getCarIcon().setNodeOrientation(pT.getRate() != 1 ? NodeOrientation.LEFT_TO_RIGHT : NodeOrientation.RIGHT_TO_LEFT);
                
            sT.add(pT);

        }
        
        sT.add(new PauseTransition(Duration.seconds(5)));
        System.out.println("Animation contains: "+sT.size());
        
    }

    public ArrayList<Animation> getAnimationSequence() {
        return sT;
    }

    
    public void playAnimationSequence(){
        
//        ArrayBlockingQueue<Animation> playList = new ArrayBlockingQueue(sT.size(), true, sT );
        for(Animation a: sT){
            int currentIndex = sT.indexOf(a);
            if(currentIndex < (sT.size()-1)){
                Animation nextAnimation = sT.get(++currentIndex);
                a.setOnFinished(e ->nextAnimation.play());
            }else
                a.setOnFinished(e -> printOccupancyArray());

        }
        
        sT.get(0).play();
        
    }

    public ArrayList<Node> getControlSystem(){
        ImageView firstBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView secondBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView entExtBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView extExtBarrier = new ImageView(new Image(getClass().getResourceAsStream("assets/Access Control Barrier.png")));
        ImageView camera = new ImageView(new Image(getClass().getResourceAsStream("assets/Camera Sensor.png")));
        
        VACS_Components.addAll(Arrays.asList(firstBarrier, secondBarrier, entExtBarrier, extExtBarrier, camera));
        
        return VACS_Components;
    }
    
    public void setControlSystemCoordinates(){
        for(Node n: VACS_Components){
            int index = VACS_Components.indexOf(n);
            switch(index){
                case 0:
                    n.setLayoutX(645);
                    n.setLayoutY(436);
                    break;
                case 1:
                    n.setLayoutX(645);
                    n.setLayoutY(318);
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
                    break;
                    
            }
        }
    }
    
    private static String[] vehicleFileNames = {"Vehicle Coupe Grey", "Vehicle Limosin White", "Vehicle Sedan Green", "Vehicle Truck RedWhite", "Vehicle Van Blue", "Vehicle Van LightBlue"};
    
    private static int[][] occupany = {{0,0,0,0,0,},
        {0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0,0,0,0},
        {0,0,0,0,0}};
    
    private void printOccupancyArray(){
        for (int i = 0; i < occupany.length; i++) {
            System.out.println("");
            for (int j = 0; j < occupany[i].length; j++) {
                System.out.print(occupany[i][j]+" ");
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
                updateOccupancy(allottedSpace, Car.occupanyStatus.PARKED);
            }
            
            int vehicleID = (int)(Math.random() * 6);
            Car car = new Car(vehicleFileNames[vehicleID]);
            car.setAllocateSpace(allottedSpace);
            parkedVehicles.add(car);
        }
    }
    
    public void layoutParkedCars(Pane pane){
        for(Car c: parkedVehicles){
            String allottedSpace = c.getAllocateSpace();
            allottedSpace = "entP_to"+allottedSpace;
            Path path = getPath(allottedSpace);
            Point2D coord = new Point2D(0,0);
            for(PathElement e: path.getElements()){
//                if(e instanceof CubicCurveTo && path.getElements().indexOf(e) == (path.getElements().size() - 1)){
                if(e instanceof MoveTo){
                    pane.getChildren().add(c);
                    c.setLayoutX(((MoveTo) e).getX());
                    c.setLayoutY(((MoveTo) e).getY());
                    
                    System.out.println(c.getVehicleModel()+" layoutX: "+c.getLayoutX()+"LayoutY: "+c.getLayoutY());
                }
            }
        }
    }
    
    private final static ArrayList<Car> parkedVehicles = new ArrayList<>();

    public static ArrayList<Car> getParkedVehicles() {
        return parkedVehicles;
    }
    
    public void setNumOfCheckIn(int numOfVehicles){
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
                System.out.println("Generated space number: "+numCode+" for generated character "+ builder.charAt(0));
                builder.append(numCode);
            }
        }
        return builder.toString();
    }
    
    private void updateOccupancy(String allottedSpace, Car.occupanyStatus status){
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
    
    
    public static void setNumOfCheckOut(){}
}