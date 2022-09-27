package com.example.physicssimulation;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class Waves extends Application {
    static String menuPic, homePic, helpPic, nMenuPic, mainStageIcon, helpStageIcon;
    static Color omori = Color.web("#6c0ffe");
    static Circle c;
    static Timer timer;
    static Line vertGrid, horiGrid, xIndic, yIndic;
    static Text xL, yL, hoverInstruction;
    static Font font = Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14);
    static Font axisTitleFont = Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 10);

    static ArrayList<Circle> standList = new ArrayList<>();
    static ArrayList<Circle> travelList = new ArrayList<>();
    static ArrayList<Circle> reTravelList = new ArrayList<>();
    static ArrayList<Line> hLineList = new ArrayList<>();
    static ArrayList<Line> vLineList = new ArrayList<>();
    static ArrayList<Line> xIndiList = new ArrayList<>();
    static ArrayList<Line> yIndiList = new ArrayList<>();
    static ArrayList<Text> xLabelList = new ArrayList<>();
    static ArrayList<Text> yLabelList = new ArrayList<>();
    static Text ampTxt, periodTxt, omegaTxt, lambdaTxt, kTxt;
    static CheckMenuItem modeNumberOne, modeNumberTwo, modeNumberThree, modeNumberFour, modeNumberFive, modeNumberSix;
    static int startPauseCounter, incidentCounter, reflectedCounter, standingCounter = 0;
    static double modeNumberOneValue, modeNumberTwoValue, modeNumberThreeValue, modeNumberFourValue, modeNumberFiveValue,
            modeNumberSixValue, A, f, T, v, omega, lambda, k, timeElapsed, timeWhenStopped, totalTimeWhenStopped;
    static Pane wavesPane;
    static Button playBtn;

    Waves(Stage travelWaveStage) {
        start(travelWaveStage);
    }

    @Override
    public void start(Stage stage) {
        playBtn = new Button("Play");
        playBtn.setId("controlButton");
        wavesPane = new Pane();
        wavesPane.setPrefWidth(960);
        wavesPane.setPrefHeight(384);

        JSONParser jsonParser = new JSONParser();
        try {
            Object o = jsonParser.parse(new InputStreamReader(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("index.json"))));
            JSONObject jsonObject = (JSONObject) o;
            menuPic = (String) jsonObject.get("menuIcon");
            homePic = (String) jsonObject.get("homeIcon");
            helpPic = (String) jsonObject.get("newtonSecondStagePic");
            nMenuPic = (String) jsonObject.get("nMenuIcon");
            mainStageIcon = (String) jsonObject.get("mainStageIcon");
            helpStageIcon = (String) jsonObject.get("helpStageIcon");
            modeNumberOneValue = Double.parseDouble((String) jsonObject.get("modeNumberOneValue"));
            modeNumberTwoValue = Double.parseDouble((String) jsonObject.get("modeNumberTwoValue"));
            modeNumberThreeValue = Double.parseDouble((String) jsonObject.get("modeNumberThreeValue"));
            modeNumberFourValue = Double.parseDouble((String) jsonObject.get("modeNumberFourValue"));
            modeNumberFiveValue = Double.parseDouble((String) jsonObject.get("modeNumberFiveValue"));
            modeNumberSixValue = Double.parseDouble((String) jsonObject.get("modeNumberSixValue"));
        }
        catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Pane navigationHBox = new Pane();

        Menu settings = new Menu("Settings");
        Menu raysVis = new Menu("Toggle Waves");
        CheckMenuItem toggleIncident = new CheckMenuItem("Incident");
        CheckMenuItem toggleReflected = new CheckMenuItem("Reflected");
        CheckMenuItem toggleStanding = new CheckMenuItem("Standing");
        toggleIncident.setSelected(true);
        toggleReflected.setSelected(true);
        toggleStanding.setSelected(true);
        raysVis.getItems().addAll(toggleIncident, toggleReflected, toggleStanding);

        Menu modeNumber = new Menu("Mode Shapes");
        modeNumberOne = new CheckMenuItem("Mode Shape 1");
        modeNumberTwo = new CheckMenuItem("Mode Shape 2");
        modeNumberThree = new CheckMenuItem("Mode Shape 3");
        modeNumberFour = new CheckMenuItem("Mode Shape 4");
        modeNumberFive = new CheckMenuItem("Mode Shape 5");
        modeNumberSix = new CheckMenuItem("Mode Shape 6");
        modeNumber.getItems().addAll(modeNumberOne, modeNumberTwo, modeNumberThree, modeNumberFour, modeNumberFive,
                modeNumberSix);

        Menu objects = new Menu("Grid Display");
        CheckMenuItem gridOn = new CheckMenuItem("On");
        CheckMenuItem gridOff = new CheckMenuItem("Off");
        gridOn.setSelected(true);
        objects.getItems().addAll(gridOn, gridOff);

        Menu theme = new Menu("Theme");
        CheckMenuItem lightMode = new CheckMenuItem("Light Mode");
        CheckMenuItem darkMode = new CheckMenuItem("Dark Mode");
        lightMode.setSelected(true);
        theme.getItems().addAll(lightMode, darkMode);

        Menu showForces = new Menu("Time");
        CheckMenuItem slowMotion = new CheckMenuItem("Slow Motion");
        CheckMenuItem normalMotion = new CheckMenuItem("Normal");
        CheckMenuItem fastMotion = new CheckMenuItem("Fast Motion");
        normalMotion.setSelected(true);
        showForces.getItems().addAll(slowMotion, normalMotion, fastMotion);
        settings.getItems().addAll(raysVis, modeNumber, objects, theme, showForces);

        MenuItem menuHelp = new MenuItem("Help");
        ScrollPane helpPane = new ScrollPane();
        helpPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        helpPane.setPadding(new Insets(10));
        VBox theoVbox = new VBox(10);

        Text theoryTxt = new Text("""

                    To interact with the standing wave simulation, simply press ‘Start’ after adjusting the values on the three sliders. In this simulation,
                there are sliders for each of the following parameters: amplitude, wave speed, and mode number. Adjusting these parameters will result in
                a standing wave being formed.
                
                    In the settings of this simulation, there will be options to switch between light and dark mode, toggle the visibility of the travelling
                and standing waves, toggle the cartesian plane, as well as an option to change the speed of the animation.

                * Amplitude [m]: The maximum distance travelled by a particle of a wave from its equilibrium position.
                * Wave Speed [m/s]: The distance the disturbance travels across a medium in a given amount of time.
                * Frequency [Hz]: The number of waves that go through a certain point on a graph per second.
                """);
        theoryTxt.setFont(font);

        Text wavesEgDesc = new Text("Pictured below are visual representations of two travelling waves (green and " +
                "blue) that form a standing wave (red).");
        wavesEgDesc.setFont(font);

        ImageView wavesEg = new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Waventerference.gif/620px-Waventerference.gif"));
        wavesEg.setFitWidth(980);
        wavesEg.setFitHeight(265 * (49/31));

        Text theoryTxt2 = new Text("""
                                    
                    First, consider that waves always originate from some sort of event or disturbance. When two waves with the same amplitude and
                frequency move in opposite directions, they will respect the superposition principle and create what is known as an interference
                pattern. To elaborate, these two linear waves will either be added together or cancelled out.

                    Interestingly enough, it is possible to come across this phenomena in daily life. Interference patterns are so common in daily life, and
                can be produced by water, sound, and even light.

                    More in-depth concepts related to waves can be found through articles written by Stephen Cohen, teacher at Vanier College, on his
                website, The Engineer’s Pulse, under the “For Physics Students” section:
                """);
        theoryTxt2.setFont(font);

        Hyperlink cohenBlog = new Hyperlink("theengineerspulse.blogspot.com/p/for-physics-students.html");
        cohenBlog.setFocusTraversable(false);
        cohenBlog.setFont(font);
        cohenBlog.setTextFill(Color.DODGERBLUE);
        cohenBlog.setOnAction(actionEvent -> {
            getHostServices().showDocument(cohenBlog.getText());
        });

        Text theoEqn = new Text("""      
                          
                Standing Wave: y(x, t) = 2Asin(kx)cos(ωt) [m]
                Traveling Wave: y(x, t) = Asin(kx ± ωt) [m]

                Period: T = 1 / f [s]
                Wave Speed: v = λf [m/s]
                Wave Number: k = 2π/λ [rad/m]
                Angular Frequency: ⍵ = 2πf [rad/s]
                               
                Where:
                x [m]: Particle's x-axis position at a certain time
                y [m]: Particle's y-axis position at a certain time
                k [rad/m]: Wave number
                A [m]: Amplitude
                t [s]: Time elapsed
                ω [rad/s]: Angular frequency
                """);
        theoEqn.setFont(font);

        Line helpSeparator = new Line(0, 0, 1020, 0);

        Text helpTitle = new Text("Standing Waves Simulator Help");
        helpTitle.setFont(font);
        helpTitle.setUnderline(true);
        Text formulaTitle = new Text("Common Formulas Used Relating to Waves");
        formulaTitle.setFont(font);
        formulaTitle.setUnderline(true);
        Text sourcesTitle = new Text("Additional Sources Consulted");
        sourcesTitle.setFont(font);
        sourcesTitle.setUnderline(true);

        Text profDaveRef = new Text("\nYoutube - Professor Dave Explains: Interference, Reflection, and Diffraction");
        profDaveRef.setFont(font);
        Hyperlink profDaveLink = new Hyperlink("www.youtube.com/watch?v=eW5VGGJuWtQ&t=195s");
        profDaveLink.setFocusTraversable(false);
        profDaveLink.setFont(font);
        profDaveLink.setTextFill(Color.DODGERBLUE);
        profDaveLink.setOnAction(actionEvent -> {
            getHostServices().showDocument(profDaveLink.getText());
        });

        Text britAmpRef = new Text("\nBrittanica.com - Physics: Amplitude");
        britAmpRef.setFont(font);
        Hyperlink britAmpLink = new Hyperlink("www.britannica.com/science/amplitude-physics");
        britAmpLink.setFocusTraversable(false);
        britAmpLink.setFont(font);
        britAmpLink.setTextFill(Color.DODGERBLUE);
        britAmpLink.setOnAction(actionEvent -> {
            getHostServices().showDocument(britAmpLink.getText());
        });

        Text britWaveRef = new Text("\nBrittanica.com - Physics: Standing Wave");
        britWaveRef.setFont(font);
        Hyperlink britWaveLink = new Hyperlink("www.britannica.com/science/standing-wave-physics");
        britWaveLink.setFocusTraversable(false);
        britWaveLink.setFont(font);
        britWaveLink.setTextFill(Color.DODGERBLUE);
        britWaveLink.setOnAction(actionEvent -> {
            getHostServices().showDocument(britWaveLink.getText());
        });

        Line helpSeparator2 = new Line(0, 0, 1020, 0);


        theoVbox.getChildren().addAll(helpTitle, theoryTxt, wavesEgDesc, wavesEg, theoryTxt2, cohenBlog, helpSeparator2, formulaTitle, theoEqn, helpSeparator,
                sourcesTitle, profDaveRef, profDaveLink, britAmpRef, britAmpLink, britWaveRef, britWaveLink);
        helpPane.setContent(theoVbox);

        Scene theoryScene = new Scene(helpPane, 1020, 650);
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Help");
        secondaryStage.setResizable(false);
        secondaryStage.setScene(theoryScene);
        secondaryStage.getIcons().add(new Image(helpStageIcon));
        menuHelp.setOnAction(actionEvent -> secondaryStage.show());

        MenuItem exitProgram = new MenuItem("Exit Program");
        MenuButton menuBtn = new MenuButton("Menu", null, settings, menuHelp, exitProgram);
        Image menuIcon = new Image(menuPic);
        Image nMenuIcon = new Image(nMenuPic);
        ImageView menuView = new ImageView(menuIcon);
        menuView.setPreserveRatio(true);
        menuView.setFitHeight(25);
        menuBtn.setGraphic(menuView);
        menuBtn.setId("navigation");

        Image homeIcon = new Image(homePic);
        ImageView homeView = new ImageView(homeIcon);
        homeView.setPreserveRatio(true);
        homeView.setFitHeight(80);
        Button homeBtn = new Button();
        homeBtn.setGraphic(homeView);
        homeBtn.setId("navigation");

        Text simTitle = new Text("Standing Wave Simulator");
        simTitle.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        navigationHBox.getChildren().addAll(menuBtn, simTitle, homeBtn);
        menuBtn.setLayoutX(10);
        homeBtn.setLayoutX(910);
        simTitle.setLayoutX(355);
        simTitle.setLayoutY(35);
        VBox controls = new VBox(20);
        root.setTop(controls);
        controls.getChildren().add(navigationHBox);

        HBox ui = new HBox(120);
        ui.setAlignment(Pos.CENTER);
        controls.getChildren().add(ui);

        Text coordinateTxt = new Text("Point Coordinates (x, y) = (           ,         )");
        Text t = new Text("Resulting Standing Wave: ");
        ampTxt = new Text("Amplitude = 0.00 cm");
        periodTxt = new Text("Period = 0.00 s");
        omegaTxt = new Text("Angular Frequency = 0.00 rad/s");
        lambdaTxt = new Text("Wavelength = 0.00 cm");
        kTxt = new Text("Wave Number = 0.00 rad/cm");
        t.setId("texts");
        ampTxt.setId("texts");
        coordinateTxt.setId("texts");
        periodTxt.setId("texts");
        omegaTxt.setId("texts");
        lambdaTxt.setId("texts");
        kTxt.setId("texts");

        Pane sliders = new Pane();

        VBox freqVBox = new VBox(5);
        freqVBox.setAlignment(Pos.CENTER);
        Text freqText = new Text("""
                Traveling Wave
                Frequency [Hz]
                """);
        TextField freqTf = new TextField("0.00 Hz");
        freqTf.setAlignment(Pos.CENTER);
        freqTf.setEditable(false);
        Slider freqSldr = new Slider(0, 5, 0);
        freqSldr.setMajorTickUnit(1);
        freqSldr.setMinorTickCount(4);

        freqSldr.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                f = freqSldr.getValue();
                freqTf.setText(String.format("%.2f", freqSldr.getValue()) + " Hz");
                if (startPauseCounter > 0 && playBtn.getText() == "Play") {
                    changeCircleFormulas();
                }
                validateValues();
                disableModes();
            }
        });
        freqVBox.getChildren().addAll(freqText,freqTf, freqSldr);

        VBox wavSpdVBox = new VBox(5);
        wavSpdVBox.setAlignment(Pos.CENTER);
        Text wavSpdText = new Text("""
                   Traveling Wave
                Wave Speed [cm/s]
                """);
        TextField wavSpdTf = new TextField("0.00 cm/s");
        wavSpdTf.setAlignment(Pos.CENTER);
        wavSpdTf.setEditable(false);
        Slider wavSpdSldr = new Slider(0, 1000, 0);
        wavSpdSldr.setMajorTickUnit(100);
        wavSpdSldr.setMinorTickCount(4);

        wavSpdSldr.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                v = wavSpdSldr.getValue();
                wavSpdTf.setText(String.format("%.2f", wavSpdSldr.getValue()) + " cm/s");
                if (startPauseCounter > 0 && playBtn.getText() == "Play") {
                    changeCircleFormulas();
                }
                validateValues();
                disableModes();
            }
        });
        wavSpdVBox.getChildren().addAll(wavSpdText,wavSpdTf,wavSpdSldr);

        VBox ampVBox = new VBox(5);
        ampVBox.setAlignment(Pos.CENTER);
        Text ampText = new Text("""
                Traveling Wave
                Amplitude [cm]
                """);
        TextField ampTf = new TextField("0.00 cm");
        ampTf.setAlignment(Pos.CENTER);
        ampTf.setEditable(false);
        Slider ampSldr = new Slider(0, 60, 0);
        ampSldr.setMajorTickUnit(5);
        ampSldr.setMinorTickCount(4);

        ampSldr.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                A = ampSldr.getValue();
                ampTf.setText(String.format("%.2f", ampSldr.getValue()) + " cm");
                if (startPauseCounter > 0 && playBtn.getText() == "Play") {
                    changeCircleFormulas();
                }
                validateValues();
            }
        });

        ampVBox.getChildren().addAll(ampText,ampTf,ampSldr);
        sliders.getChildren().addAll(freqVBox, wavSpdVBox, ampVBox);
        ui.getChildren().add(sliders);
        freqVBox.setLayoutX(10);
        freqVBox.setLayoutY(-10);
        wavSpdVBox.setLayoutX(260);
        wavSpdVBox.setLayoutY(-10);
        ampVBox.setLayoutX(500);
        ampVBox.setLayoutY(-10);
        freqSldr.setId("light");
        wavSpdSldr.setId("light");
        ampSldr.setId("light");
        freqText.setId("texts");
        ampText.setId("texts");
        wavSpdText.setId("texts");
        freqTf.setId("texts");
        ampTf.setId("texts");
        wavSpdTf.setId("texts");

        Line separator = new Line(100, 190, 900, 190);
        root.getChildren().add(separator);
        Pane actionData = new Pane();
        controls.getChildren().add(actionData);

        VBox legend = new VBox(5);
        HBox incidTraveLeg = new HBox(10);
        Line inciTravLine = new Line(30, 0, 40, 0);
        inciTravLine.setStroke(Color.CORNFLOWERBLUE);
        inciTravLine.setStrokeWidth(5);
        Text inciTravLegTxt = new Text("Incident Traveling Wave");
        inciTravLegTxt.setId("texts");
        incidTraveLeg.getChildren().addAll(inciTravLine, inciTravLegTxt);

        HBox refTravLeg = new HBox(10);
        Line refTravLine = new Line(30, 30, 40, 30);
        refTravLine.setStroke(Color.ORANGERED);
        refTravLine.setStrokeWidth(5);
        Text refTravLegTxt = new Text("Reflected Traveling Wave");
        refTravLegTxt.setId("texts");
        refTravLeg.getChildren().addAll(refTravLine, refTravLegTxt);

        HBox standLeg = new HBox(10);
        Line standLine = new Line(30, 40, 40, 40);
        standLine.setStroke(Color.DARKMAGENTA);
        standLine.setStrokeWidth(5);
        Text standLegTxt = new Text("Standing Wave");
        standLegTxt.setId("texts");
        standLeg.getChildren().addAll(standLine, standLegTxt);

        hoverInstruction = new Text("Hover over a point to display its coordinates");
        hoverInstruction.setId("texts");
        legend.getChildren().addAll(incidTraveLeg, refTravLeg, standLeg);
        actionData.getChildren().add(legend);
        legend.setLayoutX(130);

        actionData.getChildren().add(playBtn);
        playBtn.setLayoutX(450);
        playBtn.setLayoutY(30);

        VBox exData = new VBox(5);

        exData.getChildren().addAll(t, ampTxt, periodTxt, omegaTxt, lambdaTxt, kTxt);
        actionData.getChildren().add(exData);
        exData.setLayoutX(650);

        for(int i = 70; i <= wavesPane.getPrefHeight() - 60; i += 20) {
            horiGrid = new Line(30, i + 2, 970, i + 2);
            horiGrid.setStroke(Color.LIGHTGRAY);
            hLineList.add(horiGrid);
            wavesPane.getChildren().add(horiGrid);
        }

        for(int i = 30; i <= 990; i += 20) {
            vertGrid = new Line(i, 72, i, wavesPane.getPrefHeight() - 73);
            vertGrid.setStroke(Color.LIGHTGRAY);
            vLineList.add(vertGrid);
            wavesPane.getChildren().add(vertGrid);
        }

        gridOn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(Line l: hLineList) {
                    l.setVisible(true);
                }
                for(Line l: vLineList) {
                    l.setVisible(true);
                }
                gridOff.setSelected(false);
                gridOn.setSelected(true);
            }
        });

        gridOff.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(Line l: hLineList) {
                    l.setVisible(false);
                }
                for(Line l: vLineList) {
                    l.setVisible(false);
                }
                gridOn.setSelected(false);
                gridOff.setSelected(true);
            }
        });

        Line xAxis = new Line();
        xAxis.setStartX(10);
        xAxis.setEndX(970);
        xAxis.setStartY(wavesPane.getPrefHeight() / 2);
        xAxis.setEndY(wavesPane.getPrefHeight() / 2);
        Text xAxisTitle = new Text("X-Position (cm)");
        xAxisTitle.setFont(axisTitleFont);
        xAxisTitle.setLayoutX(886);
        xAxisTitle.setLayoutY(xAxis.getStartY() + 130);

        Line yAxis = new Line();
        yAxis.setStartX(30);
        yAxis.setEndX(30);
        yAxis.setStartY(72);
        yAxis.setEndY(wavesPane.getPrefHeight() - 72);
        Text yAxisTitle = new Text("Y-Position (cm)");
        yAxisTitle.setFont(axisTitleFont);
        yAxisTitle.setLayoutX(0);
        yAxisTitle.setLayoutY(60);

        wavesPane.getChildren().addAll(xAxis, xAxisTitle, yAxis, yAxisTitle, hoverInstruction);
        hoverInstruction.setLayoutX(350);
        hoverInstruction.setLayoutY(60);

        for(int i = 40; i < 980; i += 20) {
            xIndic = new Line(i + 10, xAxis.getStartY() - 5, i + 10, xAxis.getStartY() + 5);
            xIndiList.add(xIndic);
            wavesPane.getChildren().add(xIndic);
        }
        for(int i = 120; i < 980; i += 100) {
            int actNb = i - 20;
            xL = new Text("" + actNb);
            xL.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 11));
            xL.setLayoutX(i);
            xL.setLayoutY(xAxis.getStartY() + 20);
            xLabelList.add(xL);
            wavesPane.getChildren().add(xL);
        }

        for(int i = (int) wavesPane.getPrefHeight() - 70; i >= 60; i -= 20) {
            yIndic = new Line(yAxis.getStartX() - 5, i - 2, yAxis.getStartX() + 5, i - 2);
            yIndiList.add(yIndic);
            wavesPane.getChildren().add(yIndic);
        }
        for(int i = 340; i > 80; i -= 20) {
            int actNb = (int) ((i - xAxis.getStartY()) / 2);
            int dd = (actNb * -1 + 14) * 2;
            yL = new Text("" + (dd));
            yL.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 11));
            yL.setLayoutY(i - 24);
            yL.setLayoutX(-5);
            yLabelList.add(yL);
            wavesPane.getChildren().add(yL);
        }

        for(int i = 30; i <= 970; i++) {
            c = new Circle(i, wavesPane.getPrefHeight()/2, 2, Color.CORNFLOWERBLUE);
            travelList.add(c);
        }
        for(Circle c: travelList) {
            wavesPane.getChildren().add(c);
        }

        for(int i = 30; i <= 970; i++) {
            c = new Circle(i, wavesPane.getPrefHeight()/2, 2, Color.ORANGERED);
            reTravelList.add(c);
        }
        for(Circle c: reTravelList) {
            wavesPane.getChildren().add(c);
        }

        for(int i = 30; i <= 970; i++) {
            c = new Circle(i, wavesPane.getPrefHeight()/2, 3, Color.DARKMAGENTA);
            standList.add(c);
        }
        for(Circle c: standList) {
            wavesPane.getChildren().add(c);
        }

        lightMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.setStyle("-fx-background-color: white");

                inciTravLine.setStroke(Color.CORNFLOWERBLUE);
                refTravLine.setStroke(Color.ORANGERED);
                standLine.setStroke(Color.DARKMAGENTA);
                separator.setStroke(Color.BLACK);
                xAxis.setStroke(Color.BLACK);
                yAxis.setStroke(Color.BLACK);

                simTitle.setFill(Color.BLACK);
                inciTravLegTxt.setFill(Color.BLACK);
                refTravLegTxt.setFill(Color.BLACK);
                freqText.setFill(Color.BLACK);
                wavSpdText.setFill(Color.BLACK);
                ampText.setFill(Color.BLACK);
                standLegTxt.setFill(Color.BLACK);
                coordinateTxt.setFill(Color.BLACK);
                periodTxt.setFill(Color.BLACK);
                omegaTxt.setFill(Color.BLACK);
                lambdaTxt.setFill(Color.BLACK);
                hoverInstruction.setFill(Color.BLACK);
                kTxt.setFill(Color.BLACK);
                xAxisTitle.setFill(Color.BLACK);
                yAxisTitle.setFill(Color.BLACK);

                helpPane.setStyle("-fx-background-color: whitesmoke");
                theoVbox.setStyle("-fx-background-color: whitesmoke");
                theoryTxt.setFill(Color.BLACK);
                wavesEgDesc.setFill(Color.BLACK);
                theoryTxt2.setFill(Color.BLACK);
                theoEqn.setFill(Color.BLACK);
                sourcesTitle.setFill(Color.BLACK);
                profDaveRef.setFill(Color.BLACK);
                britAmpRef.setFill(Color.BLACK);
                britWaveRef.setFill(Color.BLACK);
                ampTxt.setFill(Color.BLACK);
                t.setFill(Color.BLACK);

                helpSeparator.setStroke(Color.BLACK);

                menuView.setImage(menuIcon);
                menuBtn.setStyle("-fx-background-color: #ffffff;-fx-border-color: black;");
                ampTf.setStyle("-fx-background-color: #ffffff;-fx-border-color: black;");
                freqTf.setStyle("-fx-background-color: #ffffff;-fx-border-color: black;");
                wavSpdTf.setStyle("-fx-background-color: #ffffff;-fx-border-color: black;");
                freqSldr.setId("light");
                wavSpdSldr.setId("light");
                ampSldr.setId("light");

                for(Line l: xIndiList) {
                    l.setStroke(Color.BLACK);
                }
                for(Text t: xLabelList) {
                    t.setFill(Color.BLACK);
                }
                for(Line l: yIndiList) {
                    l.setStroke(Color.BLACK);
                }
                for(Text t: yLabelList) {
                    t.setFill(Color.BLACK);
                }
                for(Circle c: travelList) {
                    c.setStroke(Color.CORNFLOWERBLUE);
                }
                for(Circle c: reTravelList) {
                    c.setStroke(Color.MAROON);
                }
                for(Circle c: standList) {
                    c.setStroke(Color.DARKMAGENTA);
                }

                darkMode.setSelected(false);
                lightMode.setSelected(true);
            }
        });

        darkMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.setStyle("-fx-background-color: #303030");

                simTitle.setFill(Color.WHITE);
                inciTravLegTxt.setFill(Color.WHITE);
                refTravLegTxt.setFill(Color.WHITE);
                standLegTxt.setFill(Color.WHITE);
                coordinateTxt.setFill(Color.WHITE);
                hoverInstruction.setFill(Color.WHITE);
                periodTxt.setFill(Color.WHITE);
                omegaTxt.setFill(Color.WHITE);
                lambdaTxt.setFill(Color.WHITE);
                kTxt.setFill(Color.WHITE);
                ampText.setFill(Color.WHITE);
                xAxisTitle.setFill(Color.WHITE);
                freqText.setFill(Color.WHITE);
                wavSpdText.setFill(Color.WHITE);
                yAxisTitle.setFill(Color.WHITE);
                ampTxt.setFill(Color.WHITE);
                t.setFill(Color.WHITE);

                helpPane.setStyle("-fx-background-color: #303030");
                theoVbox.setStyle("-fx-background-color: #303030");
                theoryTxt.setFill(Color.WHITE);
                wavesEgDesc.setFill(Color.WHITE);
                theoryTxt2.setFill(Color.WHITE);
                theoEqn.setFill(Color.WHITE);
                sourcesTitle.setFill(Color.WHITE);
                profDaveRef.setFill(Color.WHITE);
                britAmpRef.setFill(Color.WHITE);
                britWaveRef.setFill(Color.WHITE);

                helpSeparator.setStroke(Color.WHITE);

                ampTf.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");
                freqTf.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");
                wavSpdTf.setStyle("-fx-background-color: #303030; -fx-text-fill: white;");
                menuView.setImage(nMenuIcon);
                menuBtn.setStyle("-fx-background-color: #303030; -fx-border-color: white;");
                freqSldr.setId("dark");
                wavSpdSldr.setId("dark");
                ampSldr.setId("dark");

                separator.setStroke(Color.WHITE);
                inciTravLine.setStroke(Color.CYAN);
                refTravLine.setStroke(Color.ORANGERED);
                standLine.setStroke(omori);
                xAxis.setStroke(Color.WHITE);
                yAxis.setStroke(Color.WHITE);

                for(Line l: xIndiList) {
                    l.setStroke(Color.WHITE);
                }
                for(Text t: xLabelList) {
                    t.setFill(Color.WHITE);
                }
                for(Line l: yIndiList) {
                    l.setStroke(Color.WHITE);
                }
                for(Text t: yLabelList) {
                    t.setFill(Color.WHITE);
                }
                for(Circle c: travelList) {
                    c.setStroke(Color.CYAN);
                }
                for(Circle c: reTravelList) {
                    c.setStroke(Color.ORANGERED);
                }
                for(Circle c: standList) {
                    c.setStroke(omori);
                }

                lightMode.setSelected(false);
                darkMode.setSelected(true);
            }
        });

        toggleIncident.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                incidentCounter += 1;
                if (incidentCounter % 2 == 1) {
                    toggleIncident.setSelected(false);
                    for(Circle c: travelList) {
                        c.setVisible(false);
                    }
                }

                if (incidentCounter % 2 == 0) {
                    toggleIncident.setSelected(true);
                    for(Circle c: travelList) {
                        c.setVisible(true);
                    }
                }
            }
        });

        toggleReflected.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reflectedCounter += 1;
                if(reflectedCounter % 2 == 1) {
                    toggleReflected.setSelected(false);
                    for(Circle c: reTravelList) {
                        c.setVisible(false);
                    }
                }
                if(reflectedCounter % 2 == 0) {
                    toggleReflected.setSelected(true);
                    for(Circle c: reTravelList) {
                        c.setVisible(true);
                    }
                }
            }
        });

        toggleStanding.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                standingCounter += 1;
                if(standingCounter % 2 == 1) {
                    toggleStanding.setSelected(false);
                    for(Circle c: standList) {
                        c.setVisible(false);
                    }
                }
                if(standingCounter % 2 == 0) {
                    toggleStanding.setSelected(true);
                    for(Circle c: standList) {
                        c.setVisible(true);
                    }
                }
            }
        });

        normalMotion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                slowMotion.setSelected(false);
                fastMotion.setSelected(false);
                normalMotion.setSelected(true);
            }
        });

        fastMotion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                slowMotion.setSelected(false);
                normalMotion.setSelected(false);
                fastMotion.setSelected(true);
            }
        });

        slowMotion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fastMotion.setSelected(false);
                normalMotion.setSelected(false);
                slowMotion.setSelected(true);
            }
        });

        modeNumberAction(modeNumberOne, modeNumberTwo, modeNumberThree, modeNumberFour, modeNumberFive, modeNumberSix,
                freqSldr, wavSpdSldr, modeNumberOneValue);
        modeNumberAction(modeNumberTwo, modeNumberOne, modeNumberThree, modeNumberFour, modeNumberFive, modeNumberSix,
                freqSldr, wavSpdSldr, modeNumberTwoValue);
        modeNumberAction(modeNumberThree, modeNumberOne, modeNumberTwo, modeNumberFour, modeNumberFive, modeNumberSix,
                freqSldr, wavSpdSldr, modeNumberThreeValue);
        modeNumberAction(modeNumberFour, modeNumberOne, modeNumberTwo, modeNumberThree, modeNumberFive, modeNumberSix,
                freqSldr, wavSpdSldr, modeNumberFourValue);
        modeNumberAction(modeNumberFive, modeNumberOne, modeNumberTwo, modeNumberThree, modeNumberFour, modeNumberSix,
                freqSldr, wavSpdSldr, modeNumberFiveValue);
        modeNumberAction(modeNumberSix, modeNumberOne, modeNumberTwo, modeNumberThree, modeNumberFour, modeNumberFive,
                freqSldr, wavSpdSldr, modeNumberSixValue);

        AnimationTimer animateTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                validateValues();
                if(normalMotion.isSelected()) {
                    timeElapsed = timer.elapsed();
                }
                if(slowMotion.isSelected()) {
                    timeElapsed = timer.elapsed() / 5;
                }
                if(fastMotion.isSelected()) {
                    timeElapsed = timer.elapsed() * 5;
                }
                for(Circle c : standList) {
                    double sinTing = Math.sin(k * (c.getCenterX() - 30));
                    double cosTing = Math.cos(omega * (timeElapsed + totalTimeWhenStopped));
                    double eq = 2 * A * sinTing * cosTing;
                    c.setCenterY(192 + eq);
                }
                for(Circle c : travelList) {
                    double sinTing = Math.sin((k * (c.getCenterX() - 30)) - (omega * (timeElapsed + totalTimeWhenStopped)));
                    double eq = A * sinTing;
                    c.setCenterY(192 + eq);
                }
                for(Circle c : reTravelList) {
                    double sinTing = Math.sin((k * (c.getCenterX() - 30)) + (omega * (timeElapsed + totalTimeWhenStopped)));
                    double eq = A * sinTing;
                    c.setCenterY(192 + eq);
                }
            }
        };

        playBtn.setOnAction(actionEvent -> {
            startPauseCounter += 1;
            if(startPauseCounter % 2 == 1) {
                playBtn.setText("Pause");
                animateTimer.start();
                timer = new Timer();
                hoverInstruction.setText("Hover over a point to display its coordinates");
                hoverInstruction.setLayoutX(350);
            }
            if(startPauseCounter % 2 == 0) {
                playBtn.setText("Play");
                animateTimer.stop();
                timeWhenStopped = timer.elapsed();

                if(normalMotion.isSelected()) {
                    totalTimeWhenStopped += timeWhenStopped;
                }
                if(fastMotion.isSelected()) {
                    totalTimeWhenStopped += 10 * timeWhenStopped;

                }
                if(slowMotion.isSelected()) {
                    totalTimeWhenStopped += timeWhenStopped / 10;
                }
            }
            showCoordinates(hoverInstruction, standList);
            showCoordinates(hoverInstruction, travelList);
            showCoordinates(hoverInstruction, reTravelList);
        });

        homeBtn.setOnAction(actionEvent -> {
            stage.close();
            secondaryStage.close();
            animateTimer.stop();
            startPauseCounter = 0;
            incidentCounter = 0;
            reflectedCounter = 0;
            A = 0;
            f = 0;
            T = 0;
            v = 0;
            omega = 0;
            lambda = 0;
            k = 0;
            timeElapsed = 0;
            timeWhenStopped = 0;
            totalTimeWhenStopped = 0;
            for (Circle c : travelList) {
                c.setCenterY(192);
            }
            for (Circle c : reTravelList) {
                c.setCenterY(192);
            }
            for (Circle c : standList) {
                c.setCenterY(192);
            }
        });

        root.setCenter(wavesPane);

        Scene scene = new Scene(root, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("waves.css").toExternalForm());
        stage.setTitle("Standing Wave Simulator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image(mainStageIcon));
        stage.show();
    }

    private void modeNumberAction(CheckMenuItem modeNumberOne, CheckMenuItem modeNumberTwo,
                                  CheckMenuItem modeNumberThree, CheckMenuItem modeNumberFour,
                                  CheckMenuItem modeNumberFive, CheckMenuItem modeNumberSix,
                                  Slider freqSldr, Slider wavSpdSldr, double modeNumberOneValue) {
        modeNumberOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                freqSldr.setValue(0.50);
                wavSpdSldr.setValue(modeNumberOneValue);
                modeNumberOne.setSelected(true);
                modeNumberTwo.setSelected(false);
                modeNumberThree.setSelected(false);
                modeNumberFour.setSelected(false);
                modeNumberFive.setSelected(false);
                modeNumberSix.setSelected(false);
            }
        });
    }

    private void showCoordinates(Text coordinateTxt, ArrayList<Circle> standList) {
        for (Circle c : standList) {
            c.setOnMouseEntered(mouseEvent -> {
                coordinateTxt.setText("Point Coordinates (x, y) = (" + String.format("%.2f",
                        (c.getCenterX() - 30)) + ", " + String.format("%.2f",  (192 - c.getCenterY())) + ")");
                coordinateTxt.setLayoutX(350);
            });
            c.setOnMouseExited(mouseEvent -> {
                coordinateTxt.setText("Hover over a point to display its coordinates");
                coordinateTxt.setLayoutX(350);
            });
        }
    }

    private void changeCircleFormulas() {
        for (Circle c : standList) {
            double sinTing = Math.sin(k * (c.getCenterX() - 30));
            double cosTing = Math.cos(omega * (totalTimeWhenStopped));
            double eq = 2 * A * sinTing * cosTing;
            c.setCenterY(192 + eq);
        }
        for (Circle c : travelList) {
            double sinTing = Math.sin((k * (c.getCenterX() - 30)) - (omega * (totalTimeWhenStopped)));
            double eq = A * sinTing;
            c.setCenterY(192 + eq);
        }
        for (Circle c : reTravelList) {
            double sinTing = Math.sin((k * (c.getCenterX() - 30)) + (omega * (totalTimeWhenStopped)));
            double eq = A * sinTing;
            c.setCenterY(192 + eq);
        }
    }

    private void validateValues() {
        T = 1 / f;
        omega = 2 * Math.PI * f;
        lambda = v / f;
        k = (2 * Math.PI) / lambda;
        ampTxt.setText("Amplitude = " + String.format("%.2f", 2 * A) + " cm");
        periodTxt.setText("Period = " + String.format("%.2f", T) + " s");
        omegaTxt.setText("Angular Frequency = " + String.format("%.2f", omega) + " rad/s");
        lambdaTxt.setText("Wavelength = " + String.format("%.2f", lambda) + " cm");
        kTxt.setText("Wave Number = " + String.format("%.2f", k) + " rad/cm");
        if (Double.isInfinite(T)) {
            periodTxt.setText("Period = 0.00 s");
        }
        if (Double.isInfinite(omega)) {
            omegaTxt.setText("Angular Frequency = 0.00 rad/s");
        }
        if (Double.isInfinite(lambda)) {
            lambdaTxt.setText("Wavelength = 0.00 cm");
        }
        if (Double.isInfinite(k)) {
            kTxt.setText("Wave Number = 0.00 rad/cm");
        }
        if (Double.isNaN(T)) {
            periodTxt.setText("Period = 0.00 s");
        }
        if (Double.isNaN(omega)) {
            omegaTxt.setText("Angular Frequency = 0.00 rad/s");
        }
        if (Double.isNaN(lambda)) {
            lambdaTxt.setText("Wavelength = 0.00 cm");
        }
        if (Double.isNaN(k)) {
            kTxt.setText("Wave Number = 0.00 rad/cm");
        }
    }

    private void disableModes() {
        modeNumberOne.setSelected(false);
        modeNumberTwo.setSelected(false);
        modeNumberThree.setSelected(false);
        modeNumberFour.setSelected(false);
        modeNumberFive.setSelected(false);
        modeNumberSix.setSelected(false);
    }
}