/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
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
public class ACS extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        PathParser parser = new PathParser();
//        SAXParser p = new SAXParser();
            JavaFXPathElementHandler handler = new JavaFXPathElementHandler("track");
            parser.setPathHandler(handler);

            String pathData = "";
            try {
                String xmlParser = XMLResourceDescriptor.getXMLParserClassName();
                SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(xmlParser);

                String uri = "File:\\Users\\PETER-PC\\Documents\\NetBeansProjects\\PCore_SVG_Test\\src\\pcore_svg_test\\assets\\FloorPlanDrivePath.svg";
                FileInputStream fI = new FileInputStream("C:\\Users\\PETER-PC\\Documents\\NetBeansProjects\\PCore_SVG_Test\\src\\pcore_svg_test\\assets\\FloorPlanDrivePath.svg");
                System.out.println(fI.available());
                SVGDocument doc = f.createSVGDocument(uri);
                Element element = doc.getElementById("entP_toA1");
                pathData = element.getAttributeNode("d").getValue();
                System.out.println(pathData);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            Pane root = new Pane();

            parser.parse(pathData);
            Path path = handler.getPath();
            root.getChildren().add(path);
            path.setTranslateX(710);
            path.setTranslateY(-175);
            path.setStroke(Color.TRANSPARENT);

            ImageView car = new ImageView(new Image(getClass().getResourceAsStream("assets/Vehicle_SedanGreen.png")));
//        car.setRotate(180);
            root.getChildren().add(car);
            ImageView map = new ImageView(new Image(getClass().getResourceAsStream("assets/FloorPlan.png")));
            map.setFitHeight(550);
            map.setPreserveRatio(true);
            root.getChildren().add(0, map);

            PathTransition pT = new PathTransition(Duration.seconds(5), path, car);
            pT.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
            pT.setCycleCount(Timeline.INDEFINITE);
            pT.setRate(-1);
            pT.play();

            primaryStage.setTitle("JavaFX PathTransition Test with SVG");
            primaryStage.setScene(new Scene(root, 740, 550));
//        primaryStage.getScene().getStylesheets().add("C:\\Users\\PETER-PC\\Documents\\NetBeansProjects\\PCore_SVG_Test\\src\\pcore_svg_test\\assets\\style.css");
            primaryStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
