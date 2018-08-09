package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;


public class BattlePane extends BorderPane implements iFinals {

    private Label minutesLeft, secondsLeft, reasultLabel, leftJudokaName, leftJudokaCountry, leftJudokaScore, rightJudokaName, rightJudokaCountry, rightJudokaScore;
    private Label leftGroundHoldDuration, rightGroundHoldDuration;
    private HBox timeLeft, timerButtons, battleEnd, leftJudokaNameBox, leftScoreButtons, leftPenaltiesBox, leftGroundHoldHBox, rightJudokaNameBox, rightScoreButtons, rightPenaltiesBox, rightGroundHoldHBox;
    private VBox timerBox, leftScoreBox, leftGroundHoldBox, leftJudokaBox, rightScoreBox, rightJudokaBox, rightGroundHoldBox;
    private Button hajimeButton, pauseButton, restartButton, newBattleButton, leftYuko, leftWaZari, leftIppon, leftPenalty, rightYuko, rightWaZari, rightIppon, rightPenalty;
    private Button leftGroundHoldButton, rightGroundHoldButton;
    private Rectangle leftSuitColor, rightSuitColor, leftJudokaGroundHoldRectangle, rightJudokaGroundHoldRectangle;
    private Rectangle[] leftPenalties, rightPenalties;

    private KeyFrame battleKeyFrame;
    private Timeline battleTimeLine;
    private EventHandler<ActionEvent> battleEventHandler;
    private int timerCount;

    public BattlePane(Battle battle, Stage primaryStage, Scene createBattleScene) {
        super();
        setBackground(new Background(new BackgroundFill(Color.DARKGRAY.darker(), CornerRadii.EMPTY, Insets.EMPTY)));
        createTimer();
        createLeftJudoka(battle);
        createRightJudoka(battle);

        super.setCenter(timerBox);
        super.setLeft(leftJudokaBox);
        super.setRight(rightJudokaBox);

        timerCount = 0;

        battleKeyFrame = new KeyFrame(Duration.millis(500), e -> {
            if (!battle.isFinished()) {
                if (timerCount % 2 == 0) {
                    battle.countTime();
                    minutesLeft.setText(String.format("%d : ", battle.getMinutesLeft()));
                    secondsLeft.setText((battle.getSecondsLeft() / 10) + "" + (battle.getSecondsLeft() % 10));
                    if (battle.getMinutesLeft() == INITIAL_STATUS && battle.getSecondsLeft() == INITIAL_SECONDS) {
                        battle.setHajime(false);
                        if (!battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                            battleTimeLine.pause();
                        }
                        if (battle.getFirstJudoka().getPts() > battle.getSecondJudoka().getPts()) {
                            whoWon(battle.getFirstJudoka(), battle);

                        } else if (battle.getFirstJudoka().getPts() < battle.getSecondJudoka().getPts()) {
                            whoWon(battle.getSecondJudoka(), battle);
                        } else {
                            if (battle.getFirstJudoka().getNumOfPenalties() > battle.getSecondJudoka().getNumOfPenalties()) {
                                whoWon(battle.getSecondJudoka(), battle);
                            } else if (battle.getFirstJudoka().getNumOfPenalties() < battle.getSecondJudoka().getNumOfPenalties()) {
                                whoWon(battle.getFirstJudoka(), battle);
                            } else {
                                battle.setGoldenScore(true);
                                battle.setFinished(false);
                                reasultLabel.setText("Golden Score");
                                reasultLabel.setTextFill(Color.GOLD);
                            }
                        }
                    }
                }
                if (battle.getFirstJudoka().isGroundHold()) {
                    groundHoldDuringBattle(battle, battle.getFirstJudoka(), leftGroundHoldDuration, leftJudokaScore, leftJudokaGroundHoldRectangle);
                } else if (battle.getSecondJudoka().isGroundHold()) {
                    groundHoldDuringBattle(battle, battle.getSecondJudoka(), rightGroundHoldDuration, rightJudokaScore, rightJudokaGroundHoldRectangle);
                }
                timerCount++;
            }
        });

        battleTimeLine = new Timeline();
        battleTimeLine.setCycleCount(Animation.INDEFINITE);
        battleTimeLine.getKeyFrames().add(battleKeyFrame);

        battleEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() == hajimeButton) {
                    if (!battle.isFinished()) {
                        if (battle.isGoldenScore()) {
                            if (battle.getFirstJudoka().getNumOfPenalties() > battle.getSecondJudoka().getNumOfPenalties()) {
                                whoWon(battle.getSecondJudoka(), battle);
                            } else if (battle.getFirstJudoka().getNumOfPenalties() > battle.getSecondJudoka().getNumOfPenalties()) {
                                whoWon(battle.getFirstJudoka(), battle);
                            } else {
                                battleTimeLine.play();
                                battle.setHajime(true);
                            }
                        } else {
                            battleTimeLine.play();
                            battle.setHajime(true);
                        }
                    }
                }
                if (event.getSource() == pauseButton) {
                    if (battle.isHajime()) {
                        battle.setHajime(false);

                        if (battle.isGoldenScore()) {
                            if (battle.getFirstJudoka().getGroundHoldDuration() >= GROUND_HOLD_YUKO) {
                                whoWon(battle.getFirstJudoka(), battle);
                            } else if (battle.getSecondJudoka().getGroundHoldDuration() >= GROUND_HOLD_YUKO) {
                                whoWon(battle.getSecondJudoka(), battle);
                            }
                        }


                        battle.getFirstJudoka().setGroundHold(false);
                        battle.getFirstJudoka().setGroundHoldDuration(-1);
                        battle.getSecondJudoka().setGroundHold(false);
                        battle.getSecondJudoka().setGroundHoldDuration(-1);

                        leftJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                        leftGroundHoldDuration.setText("");
                        rightJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                        rightGroundHoldDuration.setText("");

                        battleTimeLine.pause();
                    }
                }
                if (event.getSource() == restartButton) {
                    battle.restartBattle();
                    minutesLeft.setText(String.format("%d : ", INITIAL_MINUTES));
                    secondsLeft.setText(String.format("%d %d", INITIAL_SECONDS, INITIAL_SECONDS));
                    reasultLabel.setText("");
                    leftJudokaScore.setText(INITIAL_SCORE);
                    rightJudokaScore.setText(INITIAL_SCORE);
                    for (int i = INITIAL_STATUS; i < leftPenalties.length; i++) {
                        leftPenalties[i].setFill(Color.WHITE);
                        rightPenalties[i].setFill(Color.WHITE);
                    }
                    leftGroundHoldDuration.setText("");
                    rightGroundHoldDuration.setText("");
                    leftJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                    leftJudokaGroundHoldRectangle.setFill(battle.getFirstJudoka().getSuitColor());
                    rightJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                    rightJudokaGroundHoldRectangle.setFill(battle.getSecondJudoka().getSuitColor());

                    timerBox.getChildren().remove(battleEnd);
                }

                if (event.getSource() == newBattleButton) {
                    primaryStage.setScene(createBattleScene);
                }

                if (event.getSource() == leftYuko) {
                    if (!battle.isFinished() && !battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                        battle.getFirstJudoka().setPts(YUKO);
                        if (battle.isGoldenScore()) {
                            whoWon(battle.getFirstJudoka(), battle);
                        }
                        leftJudokaScore.setText("0 " + (battle.getFirstJudoka().getPts() / 10) % 10 + " " + battle.getFirstJudoka().getPts() % 10);
                    }
                }
                if (event.getSource() == leftWaZari) {
                    if (!battle.isFinished() && !battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                        battle.getFirstJudoka().setPts(WA_ZARI);
                        if (battle.isGoldenScore()) {
                            whoWon(battle.getFirstJudoka(), battle);
                        } else if (battle.getFirstJudoka().getWaZariCounter() > ONE) {
                            whoWon(battle.getFirstJudoka(), battle);
                        }
                        leftJudokaScore.setText((battle.getFirstJudoka().getPts() / 100) + " " + ((battle.getFirstJudoka().getPts() / 10) % 10) + " " + battle.getFirstJudoka().getPts() % 10);
                    }
                }
                if (event.getSource() == leftIppon) {
                    if (!battle.isFinished() && !battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                        battle.getFirstJudoka().setPts(IPPON);
                        whoWon(battle.getFirstJudoka(), battle);
                        leftJudokaScore.setText("1 " + (battle.getFirstJudoka().getPts() / 10) % 10 + " " + battle.getFirstJudoka().getPts() % 10);
                    }
                }
                if (event.getSource() == leftPenalty) {
                    if (!battle.isFinished() && !battle.isHajime() && battle.getMinutesLeft() < INITIAL_MINUTES) {
                        if (battle.getFirstJudoka().getNumOfPenalties() < MAX_PENALTIES_ALLOWED) {
                            leftPenalties[battle.getFirstJudoka().getNumOfPenalties()].setFill(Color.YELLOW);
                            battle.getFirstJudoka().setNumOfPenalties(battle.getFirstJudoka().getNumOfPenalties() + 1);
                        } else {
                            battle.getSecondJudoka().setPts(IPPON);
                            for (int i = 0; i < leftPenalties.length; i++) {
                                leftPenalties[i].setFill(Color.RED);
                            }
                            whoWon(battle.getSecondJudoka(), battle);
                            rightJudokaScore.setText("1 " + (battle.getSecondJudoka().getPts() / 10) % 10 + " " + battle.getSecondJudoka().getPts() % 10);
                        }
                    }
                }
                if (event.getSource() == leftGroundHoldButton) {
                    if (battle.isHajime() && !battle.isFinished()) {
                        if (!battle.getFirstJudoka().isGroundHold()) {
                            battle.getFirstJudoka().startGroundHold(battle.getSecondJudoka());
                            rightGroundHoldDuration.setText("");
                            rightJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                        } else {
                            if (battle.isGoldenScore() && battle.getFirstJudoka().getGroundHoldDuration() > GROUND_HOLD_YUKO) {
                                whoWon(battle.getSecondJudoka(), battle);
                            }
                            battle.getFirstJudoka().setGroundHold(false);
                            battle.getFirstJudoka().setGroundHoldDuration(-1);
                            leftGroundHoldDuration.setText("");
                            leftJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                        }
                    }
                }
                if (event.getSource() == rightYuko) {
                    if (!battle.isFinished() && !battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                        battle.getSecondJudoka().setPts(YUKO);
                        if (battle.isGoldenScore()) {
                            whoWon(battle.getSecondJudoka(), battle);
                        }
                        rightJudokaScore.setText("0 " + (battle.getSecondJudoka().getPts() / 10) % 10 + " " + battle.getSecondJudoka().getPts() % 10);
                    }
                }
                if (event.getSource() == rightWaZari) {
                    if (!battle.isFinished() && !battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                        battle.getSecondJudoka().setPts(WA_ZARI);
                        if (battle.isGoldenScore()) {
                            whoWon(battle.getSecondJudoka(), battle);
                        } else if (battle.getSecondJudoka().getWaZariCounter() > ONE) {
                            whoWon(battle.getSecondJudoka(), battle);
                        }
                        rightJudokaScore.setText(battle.getSecondJudoka().getPts() / 100 + " " + ((battle.getSecondJudoka().getPts() / 10) % 10) + " " + battle.getSecondJudoka().getPts() % 10);
                    }
                }
                if (event.getSource() == rightIppon) {
                    if (!battle.isFinished() && !battle.getFirstJudoka().isGroundHold() && !battle.getSecondJudoka().isGroundHold()) {
                        battle.getFirstJudoka().setPts(IPPON);
                        whoWon(battle.getSecondJudoka(), battle);
                        rightJudokaScore.setText("1 " + (battle.getSecondJudoka().getPts() / 10) % 10 + " " + battle.getSecondJudoka().getPts() % 10);
                    }
                }
                if (event.getSource() == rightPenalty) {
                    if (!battle.isFinished() && !battle.isHajime() && battle.getMinutesLeft() < INITIAL_MINUTES) {
                        if (battle.getSecondJudoka().getNumOfPenalties() < MAX_PENALTIES_ALLOWED) {
                            rightPenalties[battle.getSecondJudoka().getNumOfPenalties()].setFill(Color.YELLOW);
                            battle.getSecondJudoka().setNumOfPenalties(battle.getSecondJudoka().getNumOfPenalties() + 1);
                        } else {
                            battle.getFirstJudoka().setPts(IPPON);
                            for (int i = 0; i < rightPenalties.length; i++) {
                                rightPenalties[i].setFill(Color.RED);
                            }
                            whoWon(battle.getFirstJudoka(), battle);
                            leftJudokaScore.setText("1 " + (battle.getFirstJudoka().getPts() / 10) % 10 + " " + battle.getFirstJudoka().getPts() % 10);
                        }
                    }
                }
                if (event.getSource() == rightGroundHoldButton) {
                    if (battle.isHajime() && !battle.isFinished()) {
                        if (!battle.getSecondJudoka().isGroundHold()) {
                            battle.getSecondJudoka().startGroundHold(battle.getFirstJudoka());
                            leftGroundHoldDuration.setText("");
                            leftJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                        } else {
                            if (battle.isGoldenScore() && battle.getSecondJudoka().getGroundHoldDuration() >= GROUND_HOLD_YUKO) {
                                whoWon(battle.getSecondJudoka(), battle);
                            }
                            battle.getSecondJudoka().setGroundHold(false);
                            battle.getSecondJudoka().setGroundHoldDuration(-1);
                            rightGroundHoldDuration.setText("");
                            rightJudokaGroundHoldRectangle.setWidth(INITIAL_STATUS);
                        }
                    }
                }
            }
        };
        hajimeButton.setOnAction(battleEventHandler);
        pauseButton.setOnAction(battleEventHandler);

        leftYuko.setOnAction(battleEventHandler);
        leftWaZari.setOnAction(battleEventHandler);
        leftIppon.setOnAction(battleEventHandler);
        leftPenalty.setOnAction(battleEventHandler);
        leftGroundHoldButton.setOnAction(battleEventHandler);

        rightYuko.setOnAction(battleEventHandler);
        rightWaZari.setOnAction(battleEventHandler);
        rightIppon.setOnAction(battleEventHandler);
        rightPenalty.setOnAction(battleEventHandler);
        rightGroundHoldButton.setOnAction(battleEventHandler);
    }

    public void createRightJudoka(Battle battle) {
        rightJudokaName = new Label(battle.getSecondJudoka().getName());
        rightJudokaName.setFont(Font.font("Arial", FontWeight.NORMAL, NAMES_LABEL_FONT_SIZE));
        rightJudokaName.setPrefWidth(NAME_PREF_WIDTH);
        rightJudokaName.setAlignment(Pos.CENTER_RIGHT);

        rightSuitColor = new Rectangle(SUIT_RECTANGLE_WIDTH, rightJudokaName.getFont().getSize());
        rightSuitColor.setFill(battle.getSecondJudoka().getSuitColor());
        rightSuitColor.setStroke(Color.BLACK);

        rightJudokaNameBox = new HBox(rightSuitColor, rightJudokaName);
        rightJudokaNameBox.setSpacing(5);

        rightJudokaCountry = new Label(battle.getSecondJudoka().getCountry());
        rightJudokaCountry.setFont(Font.font("Arial", FontWeight.NORMAL, NAMES_LABEL_FONT_SIZE));

        rightJudokaScore = new Label(INITIAL_SCORE);
        rightJudokaScore.setFont(Font.font("Ariel", FontWeight.NORMAL, JUDOKA_SCORE_SIZE));
        rightJudokaScore.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        rightJudokaScore.setAlignment(Pos.CENTER);

        rightYuko = new Button(YUKO_BUTTON);
        rightWaZari = new Button(WA_ZARI_BUTTON);
        rightIppon = new Button(IPON_BUTTON);

        rightScoreButtons = new HBox(rightIppon, rightWaZari, rightYuko);
        rightScoreButtons.setPrefWidth(rightJudokaScore.getPrefWidth());
        rightScoreButtons.setSpacing(JUDOKA_BUTTONS_SPACING);
        rightScoreButtons.setAlignment(Pos.CENTER);

        rightScoreBox = new VBox(rightJudokaScore, rightScoreButtons);
        rightScoreBox.setAlignment(Pos.CENTER);

        rightPenalties = new Rectangle[MAX_PENALTIES_ALLOWED];
        for (int i = 0; i < rightPenalties.length; i++) {
            rightPenalties[i] = new Rectangle(PENALTY_RECTANGLE_SIZE, PENALTY_RECTANGLE_SIZE);
            rightPenalties[i].setFill(Color.WHITE);
            rightPenalties[i].setStroke(Color.BLACK);
        }
        rightPenalty = new Button(PENALTY_BUTTON);
        rightPenaltiesBox = new HBox(rightPenalty, rightPenalties[0], rightPenalties[1], rightPenalties[2]);
        rightPenaltiesBox.setAlignment(Pos.CENTER);

        rightGroundHoldButton = new Button(GROUND_HOLD_BUTTON);
        rightGroundHoldDuration = new Label("");
        rightGroundHoldDuration.setFont(Font.font("Times", FontWeight.EXTRA_BOLD, 20));

        rightGroundHoldHBox = new HBox(rightGroundHoldDuration, rightGroundHoldButton);
        rightGroundHoldHBox.setSpacing(30);
        rightGroundHoldHBox.setAlignment(Pos.CENTER_RIGHT);

        rightJudokaGroundHoldRectangle = new Rectangle(0, 50);
        rightJudokaGroundHoldRectangle.setFill(battle.getSecondJudoka().getSuitColor());

        rightGroundHoldBox = new VBox(rightGroundHoldHBox, rightJudokaGroundHoldRectangle);
        rightGroundHoldBox.setSpacing(5);
        rightGroundHoldBox.setAlignment(Pos.CENTER_RIGHT);

        rightJudokaBox = new VBox(rightJudokaNameBox, rightJudokaCountry, rightScoreBox, rightPenaltiesBox, rightGroundHoldBox);
        rightJudokaBox.setSpacing(JUDOKA_VBOX_SPACING);
        rightJudokaBox.setStyle("-fx-border-color: black");
        rightJudokaBox.setAlignment(Pos.TOP_CENTER);
        rightJudokaBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY.darker().darker(), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void createLeftJudoka(Battle battle) {
        leftJudokaName = new Label(battle.getFirstJudoka().getName());
        leftJudokaName.setFont(Font.font("Arial", FontWeight.NORMAL, NAMES_LABEL_FONT_SIZE));
        leftJudokaName.setPrefWidth(NAME_PREF_WIDTH);
        leftJudokaName.setAlignment(Pos.CENTER_LEFT);

        leftSuitColor = new Rectangle(SUIT_RECTANGLE_WIDTH, leftJudokaName.getFont().getSize());
        leftSuitColor.setFill(battle.getFirstJudoka().getSuitColor());
        leftSuitColor.setStroke(Color.BLACK);

        leftJudokaNameBox = new HBox(leftJudokaName, leftSuitColor);
        leftJudokaNameBox.setSpacing(5);

        leftJudokaCountry = new Label(battle.getFirstJudoka().getCountry());
        leftJudokaCountry.setFont(Font.font("Arial", FontWeight.NORMAL, NAMES_LABEL_FONT_SIZE));

        leftJudokaScore = new Label(INITIAL_SCORE);
        leftJudokaScore.setFont(Font.font("Ariel", FontWeight.NORMAL, JUDOKA_SCORE_SIZE));
        leftJudokaScore.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        leftYuko = new Button(YUKO_BUTTON);
        leftWaZari = new Button(WA_ZARI_BUTTON);
        leftIppon = new Button(IPON_BUTTON);

        leftScoreButtons = new HBox(leftIppon, leftWaZari, leftYuko);
        leftScoreButtons.setPrefWidth(leftJudokaScore.getPrefWidth());
        leftScoreButtons.setSpacing(JUDOKA_BUTTONS_SPACING);
        leftScoreButtons.setAlignment(Pos.CENTER);

        leftScoreBox = new VBox(leftJudokaScore, leftScoreButtons);
        leftScoreBox.setAlignment(Pos.CENTER);

        leftPenalties = new Rectangle[MAX_PENALTIES_ALLOWED];
        for (int i = 0; i < leftPenalties.length; i++) {
            leftPenalties[i] = new Rectangle(PENALTY_RECTANGLE_SIZE, PENALTY_RECTANGLE_SIZE);
            leftPenalties[i].setFill(Color.WHITE);
            leftPenalties[i].setStroke(Color.BLACK);
        }
        leftPenalty = new Button(PENALTY_BUTTON);
        leftPenaltiesBox = new HBox(leftPenalty, leftPenalties[0], leftPenalties[1], leftPenalties[2]);
        leftPenaltiesBox.setAlignment(Pos.CENTER);

        leftGroundHoldButton = new Button(GROUND_HOLD_BUTTON);
        leftGroundHoldDuration = new Label("");
        leftGroundHoldDuration.setFont(Font.font("Times", FontWeight.EXTRA_BOLD, 20));

        leftGroundHoldHBox = new HBox(leftGroundHoldButton, leftGroundHoldDuration);
        leftGroundHoldHBox.setSpacing(30);
        leftGroundHoldHBox.setAlignment(Pos.CENTER_LEFT);

        leftJudokaGroundHoldRectangle = new Rectangle(0, 50);
        leftJudokaGroundHoldRectangle.setFill(battle.getFirstJudoka().getSuitColor());

        leftGroundHoldBox = new VBox(leftGroundHoldHBox, leftJudokaGroundHoldRectangle);
        leftGroundHoldBox.setSpacing(5);
        leftGroundHoldBox.setAlignment(Pos.CENTER_LEFT);

        leftJudokaBox = new VBox(leftJudokaNameBox, leftJudokaCountry, leftScoreBox, leftPenaltiesBox, leftGroundHoldBox);
        leftJudokaBox.setSpacing(JUDOKA_VBOX_SPACING);
        leftJudokaBox.setStyle("-fx-border-color: black");
        leftJudokaBox.setAlignment(Pos.TOP_CENTER);
        leftJudokaBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY.darker().darker(), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void createTimer() {
        minutesLeft = new Label(INITIAL_MINUTES + " : ");
        secondsLeft = new Label(INITIAL_SECONDS + "" + INITIAL_SECONDS);

        minutesLeft.setFont(Font.font("Times", FontWeight.SEMI_BOLD, TIME_LABEL_FONT_SIZE));
        secondsLeft.setFont(minutesLeft.getFont());

        timeLeft = new HBox(minutesLeft, secondsLeft);
        timeLeft.setAlignment(Pos.CENTER);

        hajimeButton = new Button(HAJIME_BUTTON_TEXT);
        hajimeButton.setFont(Font.font("Times", FontWeight.LIGHT, NAMES_LABEL_FONT_SIZE));

        pauseButton = new Button(PAUSE_BUTTON_TEXT);
        pauseButton.setFont(hajimeButton.getFont());

        timerButtons = new HBox(hajimeButton, pauseButton);
        timerButtons.setAlignment(Pos.CENTER);

        reasultLabel = new Label("");
        reasultLabel.setFont(Font.font("Times", FontPosture.ITALIC, TIME_LABEL_FONT_SIZE));

        timerBox = new VBox(timeLeft, timerButtons, reasultLabel);
        timerBox.setAlignment(Pos.TOP_CENTER);
        timerBox.setStyle("-fx-border-color: black");
    }

    public void groundHoldDuringBattle(Battle battle, Judoka judoka, Label durationLabel, Label scoreLabel, Rectangle groundHoldDurationRectangle) {
        if (timerCount % 2 == 0) {
            judoka.setGroundHoldDuration(judoka.getGroundHoldDuration() + ONE);
            durationLabel.setText(String.format("%d", judoka.getGroundHoldDuration()));
            durationLabel.setTextFill(Color.BLACK);

            if (judoka.getGroundHoldDuration() == GROUND_HOLD_YUKO) {
                judoka.setPts(YUKO);
                scoreLabel.setText(String.format("0 %d %d", (judoka.getPts() / 10) % 10, judoka.getPts() % 10));
            } else if (judoka.getGroundHoldDuration() == GROUND_HOLD_WA_ZARI) {
                judoka.setPts(WA_ZARI - YUKO);
                if (judoka.getWaZariCounter() > ONE) {
                    whoWon(judoka, battle);
                    groundHoldDurationRectangle.setFill(Color.RED);
                }
                scoreLabel.setText(String.format("%d %d %d", judoka.getPts() / 100, (judoka.getPts() / 10) % 10, judoka.getPts() % 10));
            } else if (judoka.getGroundHoldDuration() == GROUND_HOLD_IPPON) {
                judoka.setPts(IPPON - WA_ZARI);
                whoWon(judoka, battle);
                scoreLabel.setText(String.format("1 %d %d", (judoka.getPts() / 10) % 10, judoka.getPts() % 10));
                battleTimeLine.pause();
                groundHoldDurationRectangle.setFill(Color.RED);
            }
        } else {
            durationLabel.setTextFill(Color.RED);
        }
        groundHoldDurationRectangle.setWidth(groundHoldDurationRectangle.getWidth() + 3.9);
    }

    public void whoWon(Judoka j, Battle battle) {
        reasultLabel.setText(String.format("%s won!", j.getName()));
        reasultLabel.setTextFill(j.getSuitColor());
        battle.setFinished(true);
        battle.setHajime(false);

        restartButton = new Button(RESTART_BUTTON);
        restartButton.setFont(Font.font("Ariel", FontPosture.ITALIC, 20));
        restartButton.setOnAction(battleEventHandler);

        newBattleButton = new Button(NEW_BATTLE_BUTTON);
        newBattleButton.setFont(Font.font("Ariel", FontPosture.ITALIC, 20));
        newBattleButton.setOnAction(battleEventHandler);

        battleEnd = new HBox(restartButton, newBattleButton);
        battleEnd.setAlignment(Pos.CENTER);

        timerBox.getChildren().add(battleEnd);
    }
}
