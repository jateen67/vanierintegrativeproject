package com.example.physicssimulation;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    static String mainStageIcon, helpStageIcon, newtonSim, wavesPic;

    @Override
    public void start(Stage primaryStage) {
        JSONParser jsonParser = new JSONParser();
        try {
            Object o = jsonParser.parse(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("index.json"))));
            JSONObject jsonObject = (JSONObject) o;
            mainStageIcon = (String) jsonObject.get("mainStageIcon");
            helpStageIcon = (String) jsonObject.get("helpStageIcon");
            newtonSim = (String) jsonObject.get("newton");
            wavesPic = (String) jsonObject.get("wavesPic");

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        Pane root = new Pane();
        Stage newtonStage = new Stage();
        Stage wavesStage = new Stage();
        Stage creditStage = new Stage();

        Label title = new Label("Physics Learning Tool");
        title.setAlignment(Pos.TOP_CENTER);
        title.setLayoutY(20);
        title.setLayoutX(360);
        title.setTextFill(Color.WHITE);

        Image newtonImg = new Image(newtonSim);
        ImageView iView = new ImageView();
        iView.setPreserveRatio(true);
        iView.setFitHeight(250);
        iView.setLayoutX(205);
        iView.setLayoutY(150);

        Color grey = Color.web("#868484");
        Button newton = new Button("Newton Simulator");
        newton.setLayoutX(250);
        newton.setLayoutY(500);

        Rectangle nr = new Rectangle();
        nr.setX(250); nr.setY(500);
        nr.setHeight(35); nr.setWidth(147);
        nr.setFill(Color.TRANSPARENT);
        nr.setArcHeight(7); nr.setArcWidth(7);
        Line nl1 = new Line();
        nl1.setStroke(grey);
        nl1.setStartX(650); nl1.setEndX(675);nl1.setStartY(500); nl1.setEndY(500);
        nl1.setVisible(false);

        Line nl2 = new Line();
        nl2.setStartX(745); nl2.setEndX(720); nl2.setStartY(535); nl2.setEndY(535);
        nl2.setVisible(false);
        nl2.setStroke(grey);

        PathTransition npt1 = new PathTransition(Duration.seconds(2), nr, nl1);
        npt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        npt1.setCycleCount(Timeline.INDEFINITE);
        npt1.setDelay(Duration.seconds(1));
        npt1.setInterpolator(Interpolator.LINEAR);
        npt1.play();

        PathTransition npt2 = new PathTransition(Duration.seconds(2), nr, nl2);
        npt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        npt2.setCycleCount(Timeline.INDEFINITE);
        npt2.setInterpolator(Interpolator.LINEAR);
        npt2.play();

        Image wavesImg = new Image(wavesPic);

        Button waves = new Button("Waves Simulator");
        waves.setLayoutX(450);
        waves.setLayoutY(500);

        Rectangle wr = new Rectangle();
        wr.setX(450); wr.setY(500);
        wr.setHeight(35); wr.setWidth(147);
        wr.setFill(Color.TRANSPARENT);
        wr.setArcHeight(7); wr.setArcWidth(7);

        Line wl1 = new Line();
        wl1.setStartX(450); wl1.setEndX(475); wl1.setStartY(500); wl1.setEndY(500);
        wl1.setVisible(false);
        wl1.setStroke(grey);

        Line wl2 = new Line();
        wl2.setStartX(545); wl2.setEndX(520); wl2.setStartY(535); wl2.setEndY(535);
        wl2.setVisible(false);
        wl2.setStroke(grey);

        PathTransition wpt1 = new PathTransition(Duration.seconds(2), wr, wl1);
        wpt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        wpt1.setCycleCount(Timeline.INDEFINITE);
        wpt1.setDelay(Duration.seconds(1));
        wpt1.setInterpolator(Interpolator.LINEAR);
        wpt1.play();

        PathTransition wpt2 = new PathTransition(Duration.seconds(2), wr, wl2);
        wpt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        wpt2.setCycleCount(Timeline.INDEFINITE);
        wpt2.setInterpolator(Interpolator.LINEAR);
        wpt2.play();

        Button authors = new Button("About Us");
        authors.setLayoutX(650);
        authors.setLayoutY(500);

        Rectangle ar = new Rectangle();
        ar.setX(650); ar.setY(500);
        ar.setHeight(35); ar.setWidth(97);
        ar.setFill(Color.TRANSPARENT);
        ar.setArcHeight(7); ar.setArcWidth(7);

        Line al1 = new Line();
        al1.setStartX(650); al1.setEndX(675); al1.setStartY(500); al1.setEndY(500);
        al1.setVisible(false);
        al1.setStroke(grey);

        Line al2 = new Line();
        al2.setStartX(745); al2.setEndX(720); al2.setStartY(535); al2.setEndY(535);
        al2.setVisible(false);
        al2.setStroke(grey);

        PathTransition apt1 = new PathTransition(Duration.millis(1500), ar, al1);
        apt1.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        apt1.setCycleCount(Timeline.INDEFINITE);
        apt1.setDelay(Duration.millis(750));
        apt1.setInterpolator(Interpolator.LINEAR);
        apt1.play();

        PathTransition apt2 = new PathTransition(Duration.millis(1500), ar, al2);
        apt2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        apt2.setCycleCount(Timeline.INDEFINITE);
        apt2.setInterpolator(Interpolator.LINEAR);
        apt2.play();
        root.getChildren().addAll(ar,nr,wr,title,waves,newton,authors,iView,al1,al2,nl1,nl2,wl1,wl2);

        Scene s = new Scene(root, 1000, 650);
        root.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #0f0c29, #302b63, #313149FF)");
        s.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        primaryStage.setScene(s);
        primaryStage.setTitle("Physics Learning Tool");
        primaryStage.getIcons().add(new Image(mainStageIcon));
        primaryStage.setResizable(false);
        primaryStage.show();

        waves.setOnAction(actionEvent -> {
            if (!wavesStage.isShowing() && !newtonStage.isShowing() && !creditStage.isShowing() ) {
                Waves w = new Waves(wavesStage);
            }
        });

        newton.setOnAction(actionEvent -> {
            if (!newtonStage.isShowing() && !wavesStage.isShowing() && !creditStage.isShowing()) {
                Newton n = new Newton(newtonStage);
            }
        });

        newton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                iView.setImage(newtonImg);
                iView.setX(-10);
                nl1.setVisible(true);
                nl2.setVisible(true);
            } else {
                iView.setImage(null);
                nl1.setVisible(false);
                nl2.setVisible(false);
            }
        });

        authors.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                iView.setX(-60);
                al1.setVisible(true);
                al2.setVisible(true);
            } else {
                iView.setImage(null);
                al1.setVisible(false);
                al2.setVisible(false);
            }
        });

        waves.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                iView.setImage(wavesImg);
                iView.setX(-60);
                wl1.setVisible(true);
                wl2.setVisible(true);
            } else {
                iView.setImage(null);
                wl1.setVisible(false);
                wl2.setVisible(false);
            }
        });

        ScrollPane root2 = new ScrollPane();
        root2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root2.setPadding(new Insets(10));
        root2.setStyle("-fx-background-color: " +
                "linear-gradient(from 25% 25% to 100% 100%, #0f0c29, #302b63, #313149FF)");
        VBox abtUsVbox = new VBox(10);
        abtUsVbox.setStyle("-fx-background-color: " +
                "linear-gradient(from 25% 25% to 100% 100%, #0f0c29, #302b63, #313149FF)");

        Text abtUsTitle = new Text("About Us");
        abtUsTitle.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
        abtUsTitle.setFill(Color.WHITE);

        Text longtext = new Text("""
                 This application is the result of a collaborative effort from Laurenz Marius Gomez, Jatin Kalsi, Giuliano Verdone, and
                 Joshua Vilda. It is created using purely Java, the JavaFX library, as well as CSS. Collectively, it was decided that a
                 learning tool should be created, and should include two detailed simulations based on physical and mathematical
                 concepts learned in previous semesters. The two simulations decided on were the standing waves simulator, as well as
                 the Newton's 2nd law simulator.
                                                             
                 Standing waves simulator created by: Laurenz Marius Gomez & Giuliano Verdone
                 Newton's 2nd law simulator created by: Jatin Kalsi & Joshua Vilda
                
                
                 Images Used:

                   Menu Icon: https://www.freeiconspng.com/uploads/menu-icon-2.png
                   Alternate Menu Icon: https://imgur.com/a/I5hY58b
                   Home Icon: https://www.freeiconspng.com/uploads/home-icon-png-31.png
                  
                   Standing Waves Simulator:
                   Help Window GIF: https://en.wikipedia.org/wiki/Standing_wave
                  
                   Newton's 2nd Law Simulator:
                   Help Window Photo: https://energywavetheory.com/wp-content/uploads/2019/10/newtons-second-law-fma.jpg
                   Light Mode Background:
                   https://www.shutterstock.com/image-vector/pixel-art-game-landscape-8bit-mounts-1286043616
                   Dark Mode Background:
                   https://www.shutterstock.com/image-vector/pixel-art-game-landscape-texas-view-1339425986
                   Car: https://www.car-revs-daily.com/2014/06/12/road-test-review-2014-kia-soul-exclaim/
                   Skateboard: https://toppng.com/free-image/skateboard-PNG-free-PNG-Images_18265
                   Motorbike: https://www.motostop.eu/moto/SUZUKI_GSX_R_1000R_ABS_2021_SPORT/21107
                  
                  
                 All photos used fall under fair dealing for strictly educational purposes.
                """);
        longtext.setFill(Color.WHITE);
        longtext.setStyle("-fx-font-family: 'Verdana'; -fx-font-size: 16");

        abtUsVbox.getChildren().addAll(abtUsTitle, longtext);
        root2.setContent(abtUsVbox);

        Scene s2 = new Scene(root2, 1000, 500);
        creditStage.setScene(s2);
        creditStage.setTitle("About Us");
        creditStage.getIcons().add(new Image(helpStageIcon));
        creditStage.setResizable(false);

        authors.setOnAction(actionEvent -> {
            if (!wavesStage.isShowing() && !newtonStage.isShowing() && !creditStage.isShowing()) {
                creditStage.show();
            }
        });
    }
}