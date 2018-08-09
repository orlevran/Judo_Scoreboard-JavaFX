package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CreateBattlePane extends BorderPane implements iFinals {
    private Label leftJudokaName, leftJudokaCountry, leftJudokaSuitColor, rightJudokaName, rightJudokaCountry, rightJudokaSuitColor, errorLabel;
    private TextField leftNameTextField, leftCountryTextField, rightNameTextField, rightCountryTextField;
    private ComboBox<String> leftSuitColorComboBox, rightSuitColorComboBox;
    private Rectangle blueRectangle, whiteRectangle;
    private HBox leftJudokaNameHBox, leftJudokaCountryHBox, leftJudokaSuitColorHBox, rightJudokaNameHBox, rightJudokaCountryHBox, rightJudokaSuitColorHBox, errorHBox;
    private VBox leftJudokaVBox, rightJudokaVBox, startBattleVBox;
    private Button startBattleButton, closeErrorStage;
    private EventHandler<ActionEvent> createBattleEventHandler;

    private Stage errorStage;
    private Scene errorScene;
    private BorderPane errorPane;

    private Judoka leftJudoka, rightJudoka;
    private Battle battle;

    private Scene battleScene;
    private BattlePane battlePane;

    public CreateBattlePane(Stage primaryStage) {
        super();

        createLeftJudokaBox();
        super.setLeft(leftJudokaVBox);
        createRightJudokaBox();
        super.setRight(rightJudokaVBox);
        startBattleButton = new Button(START_BATTLE_BUTTON_TEXT);
        startBattleButton.setFont(Font.font("Ariel", FontPosture.ITALIC, 28));
        startBattleVBox = new VBox(startBattleButton);
        startBattleVBox.setAlignment(Pos.CENTER);
        super.setBottom(startBattleVBox);
        createErrorStage();


        createBattleEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() == startBattleButton) {
                    if (leftNameTextField.getText().isEmpty() || leftCountryTextField.getText().isEmpty() || rightNameTextField.getText().isEmpty() || rightCountryTextField.getText().isEmpty()) {
                        errorLabel.setText(MISSING_INFORMATION_MESSAGE);
                        errorStage.show();
                    } else if (leftSuitColorComboBox.getValue().equals(rightSuitColorComboBox.getValue())) {
                        errorLabel.setText(SUITS_COLORS_MESSAGE);
                        errorStage.show();
                    } else {
                        createBattle();
                        thirdScene(primaryStage);
                    }
                }
                if (event.getSource() == closeErrorStage) {
                    errorStage.close();
                }
            }
        };

        startBattleButton.setOnAction(createBattleEventHandler);
        closeErrorStage.setOnAction(createBattleEventHandler);
        leftSuitColorComboBox.setOnAction(createBattleEventHandler);
        rightSuitColorComboBox.setOnAction(createBattleEventHandler);
        leftNameTextField.setOnAction(createBattleEventHandler);
        leftCountryTextField.setOnAction(createBattleEventHandler);
        rightNameTextField.setOnAction(createBattleEventHandler);
        rightCountryTextField.setOnAction(createBattleEventHandler);
    }

    public void createRightJudokaBox() {
        rightJudokaName = new Label(NAME_LABEL);
        rightJudokaName.setFont(leftJudokaName.getFont());
        rightNameTextField = new TextField();

        rightJudokaNameHBox = new HBox(rightJudokaName, rightNameTextField);
        rightJudokaNameHBox.setSpacing(5);
        rightJudokaNameHBox.setPadding(new Insets(5));
        rightJudokaNameHBox.setAlignment(Pos.CENTER);

        rightJudokaCountry = new Label(COUNTRY_LABEL);
        rightJudokaCountry.setFont(leftJudokaCountry.getFont());
        rightCountryTextField = new TextField();

        rightJudokaCountryHBox = new HBox(rightJudokaCountry, rightCountryTextField);
        rightJudokaCountryHBox.setSpacing(5);
        rightJudokaCountryHBox.setPadding(new Insets(5));
        rightJudokaCountryHBox.setAlignment(Pos.CENTER);

        rightJudokaSuitColor = new Label(SUIT_COLOR_LABEL);
        rightJudokaSuitColor.setFont(leftJudokaSuitColor.getFont());
        rightSuitColorComboBox = new ComboBox<>();
        rightSuitColorComboBox.setItems(FXCollections.observableArrayList(BLUE_COLOR, WHITE_COLOR));
        rightSuitColorComboBox.setValue(WHITE_COLOR);

        rightJudokaSuitColorHBox = new HBox(rightJudokaSuitColor, rightSuitColorComboBox);
        rightJudokaSuitColorHBox.setSpacing(5);
        rightJudokaSuitColorHBox.setPadding(new Insets(5));
        rightJudokaSuitColorHBox.setAlignment(Pos.CENTER);

        rightJudokaVBox = new VBox(rightJudokaNameHBox, rightJudokaCountryHBox, rightJudokaSuitColorHBox);
        rightJudokaVBox.setAlignment(Pos.TOP_CENTER);
        rightJudokaVBox.setStyle("-fx-border-color: blue;\n" + "-fx-border-insets: 5;\n" + "-fx-border-width: 5;");
    }

    public void createLeftJudokaBox() {
        leftJudokaName = new Label(NAME_LABEL);
        leftJudokaName.setFont(Font.font("Times", FontWeight.LIGHT, 30));
        leftNameTextField = new TextField();

        leftJudokaNameHBox = new HBox(leftJudokaName, leftNameTextField);
        leftJudokaNameHBox.setSpacing(5);
        leftJudokaNameHBox.setPadding(new Insets(5));
        leftJudokaNameHBox.setAlignment(Pos.CENTER_LEFT);

        leftJudokaCountry = new Label(COUNTRY_LABEL);
        leftJudokaCountry.setFont(Font.font("Times", FontWeight.LIGHT, 30));
        leftCountryTextField = new TextField();

        leftJudokaCountryHBox = new HBox(leftJudokaCountry, leftCountryTextField);
        leftJudokaCountryHBox.setSpacing(5);
        leftJudokaCountryHBox.setPadding(new Insets(5));
        leftJudokaCountryHBox.setAlignment(Pos.CENTER_LEFT);

        leftJudokaSuitColor = new Label(SUIT_COLOR_LABEL);
        leftJudokaSuitColor.setFont(Font.font("Times", FontWeight.LIGHT, 30));
        leftSuitColorComboBox = new ComboBox<>();
        blueRectangle = new Rectangle(leftSuitColorComboBox.getWidth(), leftSuitColorComboBox.getHeight());
        blueRectangle.setFill(Color.BLUE);
        whiteRectangle = new Rectangle(leftSuitColorComboBox.getWidth(), leftSuitColorComboBox.getHeight());
        whiteRectangle.setFill(Color.WHITE);
        leftSuitColorComboBox.setItems(FXCollections.observableArrayList(BLUE_COLOR, WHITE_COLOR));
        leftSuitColorComboBox.setValue(BLUE_COLOR);

        leftJudokaSuitColorHBox = new HBox(leftJudokaSuitColor, leftSuitColorComboBox);
        leftJudokaSuitColorHBox.setSpacing(5);
        leftJudokaSuitColorHBox.setPadding(new Insets(5));
        leftJudokaSuitColorHBox.setAlignment(Pos.CENTER_LEFT);

        leftJudokaVBox = new VBox(leftJudokaNameHBox, leftJudokaCountryHBox, leftJudokaSuitColorHBox);
        leftJudokaVBox.setAlignment(Pos.TOP_CENTER);
        leftJudokaVBox.setStyle("-fx-border-color: red;\n" + "-fx-border-insets: 5;\n" + "-fx-border-width: 5;");
    }

    public void createErrorStage() {
        errorStage = new Stage();
        errorStage.setTitle(ERROR_TITLE);
        errorStage.setAlwaysOnTop(true);
        errorPane = new BorderPane();
        errorLabel = new Label();
        errorLabel.setFont(Font.font("Times", FontWeight.BOLD, 30));
        errorLabel.setAlignment(Pos.CENTER);
        errorPane.setCenter(errorLabel);

        closeErrorStage = new Button(CLOSE_ERROR);
        closeErrorStage.setFont(Font.font("Ariel", 25));

        errorHBox = new HBox(closeErrorStage);
        errorHBox.setAlignment(Pos.CENTER);
        errorPane.setBottom(errorHBox);

        errorScene = new Scene(errorPane);
        errorStage.setScene(errorScene);
        errorStage.setAlwaysOnTop(true);
        errorStage.setResizable(false);
        errorStage.setWidth(300);
        errorStage.setHeight(200);
    }

    public Battle getBattle() {
        return battle;
    }

    public void createBattle() {
        if(!leftSuitColorComboBox.getValue().equals(rightSuitColorComboBox.getValue())){
            if(leftSuitColorComboBox.getValue().equals(BLUE_COLOR)&&rightSuitColorComboBox.getValue().equals(WHITE_COLOR)){
                leftJudoka = new Judoka(leftNameTextField.getText(), leftCountryTextField.getText(), Color.BLUE);
                rightJudoka = new Judoka(rightNameTextField.getText(), rightCountryTextField.getText(), Color.WHITE);
            } else if (leftSuitColorComboBox.getValue().equals(WHITE_COLOR)&&rightSuitColorComboBox.getValue().equals(BLUE_COLOR)){
                leftJudoka = new Judoka(leftNameTextField.getText(), leftCountryTextField.getText(), Color.WHITE);
                rightJudoka = new Judoka(rightNameTextField.getText(), rightCountryTextField.getText(), Color.BLUE);
            }
            battle = new Battle(rightJudoka, leftJudoka);
        }
    }

    public Button getStartBattleButton() {
        return startBattleButton;
    }

    public void thirdScene(Stage primaryStage) {
        battlePane = new BattlePane(this.getBattle(), primaryStage, this.getScene());

        leftNameTextField.setText("");
        rightNameTextField.setText("");
        leftCountryTextField.setText("");
        rightCountryTextField.setText("");

        leftSuitColorComboBox.setValue(BLUE_COLOR);
        rightSuitColorComboBox.setValue(WHITE_COLOR);

        battleScene = new Scene(battlePane);
        primaryStage.setScene(battleScene);
    }
}
