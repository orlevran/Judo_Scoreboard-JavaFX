package sample;

import java.util.Stack;

public class Battle implements iFinals {
    private Judoka firstJudoka, secondJudoka;
    private boolean hajime, goldenScore, finished;
    private int minutesLeft, secondsLeft;

    public Battle(Judoka l, Judoka r) {
        firstJudoka = new Judoka(r);
        secondJudoka = new Judoka(l);
        hajime = false;
        goldenScore = false;
        finished = false;
        minutesLeft = INITIAL_MINUTES;
        secondsLeft = INITIAL_SECONDS;
    }

    public void countTime() {
        if (isHajime() && !isGoldenScore()) {
            this.secondsLeft--;
            if (getSecondsLeft() == INITIAL_SECONDS && getMinutesLeft() == INITIAL_SECONDS) {
                setHajime(false);
            } else if (getSecondsLeft() < INITIAL_SECONDS) {
                this.secondsLeft = MINUTE_LESS;
                this.minutesLeft--;
            }
        } else if (isHajime() && isGoldenScore()) {
            this.secondsLeft++;
            if (getSecondsLeft() > MINUTE_LESS) {
                this.secondsLeft = INITIAL_SECONDS;
                this.minutesLeft++;
            }
        }
    }

    public void restartBattle(){
        this.minutesLeft = INITIAL_MINUTES;
        this.secondsLeft = INITIAL_SECONDS;
        setHajime(false);
        setGoldenScore(false);
        setFinished(false);

        getFirstJudoka().resetScore();
        getFirstJudoka().setGroundHoldDuration(-1);
        getFirstJudoka().setGroundHold(false);
        getFirstJudoka().setNumOfPenalties(INITIAL_STATUS);
        getFirstJudoka().setWaZariCounter(INITIAL_STATUS);

        getSecondJudoka().resetScore();
        getSecondJudoka().setGroundHoldDuration(-1);
        getSecondJudoka().setGroundHold(false);
        getSecondJudoka().setNumOfPenalties(INITIAL_STATUS);
        getSecondJudoka().setWaZariCounter(INITIAL_STATUS);
    }

    public void setGoldenScore(boolean b) {
        goldenScore = b;
    }

    public void setFinished(boolean b) {
        finished = b;
    }

    public void setHajime(boolean b) {
        hajime = b;
    }

    public boolean isGoldenScore() {
        return goldenScore;
    }

    public boolean isHajime() {
        return hajime;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public Judoka getFirstJudoka() {
        return firstJudoka;
    }

    public Judoka getSecondJudoka() {
        return secondJudoka;
    }
}
