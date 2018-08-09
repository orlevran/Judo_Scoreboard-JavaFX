package sample;

public interface iFinals {
    static final int INITIAL_MINUTES = 5, INITIAL_SECONDS = 0, MINUTE_LESS = 59;
    static final String HAJIME_BUTTON_TEXT = "Hajime!", PAUSE_BUTTON_TEXT = "Pause", INITIAL_SCORE = "0 0 0", RESTART_BUTTON = "Restart", NEW_BATTLE_BUTTON = "New Battle";
    static final int NAMES_LABEL_FONT_SIZE = 20, TIME_LABEL_FONT_SIZE = 50, JUDOKA_SCORE_SIZE = 35, JUDOKA_BUTTONS_SPACING = 2;
    static final int NAME_PREF_WIDTH = 120, SUIT_RECTANGLE_WIDTH = 40;
    static final String YUKO_BUTTON = "Y", WA_ZARI_BUTTON = "W", IPON_BUTTON = "I", PENALTY_BUTTON = "P", GROUND_HOLD_BUTTON = "Ground Hold";
    static final int JUDOKA_VBOX_SPACING = 10, ONE = 1;

    static final int YUKO = 1, WA_ZARI = 10, IPPON = 100;
    static final int MAX_PENALTIES_ALLOWED = 3, REQUIRED_WA_ZARI = 1, GROUND_HOLD_YUKO = 10, GROUND_HOLD_WA_ZARI = 15, GROUND_HOLD_IPPON = 20;
    static final int INITIAL_STATUS = 0;
    static final int YUKO_DURATION = 10, WA_ZARI_DURATION = 15, IPPON_DURATION = 20;
    static final String NAME_LABEL = "Name: ", COUNTRY_LABEL = "Country: ", SUIT_COLOR_LABEL = "Suit's color: ";
    static final String START_BATTLE_BUTTON_TEXT = "GO!";

    static final String BLUE_COLOR = "Blue", WHITE_COLOR = "White";

    static final int PENALTY_RECTANGLE_SIZE = 20;
    static final String ERROR_TITLE = "Error", MISSING_INFORMATION_MESSAGE = "   Missing\ninformation", SUITS_COLORS_MESSAGE = "Chose different\n   suits colors", CLOSE_ERROR = "OK";
}
