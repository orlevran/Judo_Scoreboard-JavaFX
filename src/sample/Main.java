package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedOutputStream;


public class Main extends Application implements iFinals {
    private static Scene mainScene, createBattleScene;
    private static CreateBattlePane createBattlePane;
    public static BorderPane mainPane;
    private static Label welcomeLabel;
    private static Button createBattleButton;

    private static Timeline mainTimeLine;
    private static KeyFrame mainKeyFrame;
    private static int counter = 0;
    private static Image[] images;
    private static ImageView imageView;
    private static EventHandler<ActionEvent> mainEventHandler;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Judo");
        createGUI(primaryStage);
        primaryStage.setWidth(650);
        primaryStage.setHeight(400);
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void createGUI(Stage primaryStage) {
        mainPane = new BorderPane();
        welcomeLabel = new Label("Welcome!");
        welcomeLabel.setTextFill(Color.RED);
        welcomeLabel.setFont(Font.font("Times", FontPosture.ITALIC, 40));
        createBattleButton = new Button("Start");
        createBattleButton.setFont(Font.font("Ariel", FontPosture.REGULAR, 35));

        VBox mainVB = new VBox(welcomeLabel, createBattleButton);
        mainVB.setPadding(new Insets(10));
        mainVB.setSpacing(5);
        mainVB.setAlignment(Pos.CENTER);

        images = new Image[3];
        images[0] = new Image("JudoImage001.jpg");
        images[1] = new Image("JudoImage002.jpg");
        images[2] = new Image("JudoImage003.jpg");

        imageView = new ImageView();
        mainPane.getChildren().add(imageView);
        imageView.setFitWidth(primaryStage.getWidth());
        imageView.setFitHeight(primaryStage.getHeight());

        mainKeyFrame = new KeyFrame(Duration.seconds(5), e -> {
            ((ImageView)(mainPane.getChildren().get(0))).setFitHeight(primaryStage.getHeight());
            ((ImageView)(mainPane.getChildren().get(0))).setFitWidth(primaryStage.getWidth());
            ((ImageView)(mainPane.getChildren().get(0))).setImage(images[getCounter()]);
            setCounter(getCounter() + ONE, images.length );
        });
        mainTimeLine = new Timeline();
        mainTimeLine.getKeyFrames().add(mainKeyFrame);
        mainTimeLine.setCycleCount(Animation.INDEFINITE);
        mainTimeLine.play();

        mainPane.setCenter(mainVB);
        mainScene = new Scene(mainPane);

        mainEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() == createBattleButton) {
                    secondSceneCreator(primaryStage);
                    mainTimeLine.pause();
                }
            }
        };
        createBattleButton.setOnAction(mainEventHandler);
    }

    public static void secondSceneCreator(Stage primaryStage) {
        createBattlePane = new CreateBattlePane(primaryStage);
        createBattleScene = new Scene(createBattlePane);
        primaryStage.setScene(createBattleScene);
    }

    public static void setCounter(int c, int limit) {
        if (c >= limit) {
            c = 0;
        }
        counter = c;
    }

    public static int getCounter() {
        return counter;
    }

    public static void saveToFile(){
        BufferedOutputStream writer = null;
    }
}