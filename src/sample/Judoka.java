package sample;

import javafx.scene.paint.Color;

public class Judoka implements iFinals {
    private String name;
    private String country;
    private Color suitColor;
    private int pts, numOfPenalties, waZariCounter, groundHoldDuration;
    private boolean groundHold;

    public Judoka(String name, String country, Color c) {
        this.name = name;
        this.country = country;
        this.pts = INITIAL_STATUS;
        this.numOfPenalties = INITIAL_STATUS;
        this.waZariCounter = INITIAL_STATUS;
        this.groundHoldDuration = -1;
        this.groundHold = false;
        this.suitColor = c;
    }

    public Judoka(Judoka j) {
        this.name = j.getName();
        this.country = j.getCountry();
        this.suitColor = j.getSuitColor();
        this.pts = INITIAL_STATUS;
        this.numOfPenalties = INITIAL_STATUS;
        this.waZariCounter = INITIAL_STATUS;
        this.groundHoldDuration = -1;
        this.groundHold = false;
    }


    public void startGroundHold(Judoka competitor) {
        this.setGroundHold(true);
        competitor.setGroundHold(false);
        competitor.setGroundHoldDuration(-1);
    }

    public void penaltiesCheck(Judoka competitor) {
        if (this.getNumOfPenalties() == MAX_PENALTIES_ALLOWED) {
            competitor.setPts(IPPON);
        }
        this.numOfPenalties++;
    }

    public void waZariCheck() {
        if (getWaZariCounter() == REQUIRED_WA_ZARI) {
            this.pts -= WA_ZARI;
            this.pts += IPPON;
        } else {
            this.pts += WA_ZARI;
        }
        this.waZariCounter++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSuitColor(Color c) {
        if (c.equals(Color.WHITE) || c.equals(Color.BLUE)) {
            this.suitColor = c;
        }
    }

    public void setNumOfPenalties(int number) {
        if (number >= INITIAL_STATUS && number <= MAX_PENALTIES_ALLOWED + 1) {
            this.numOfPenalties = number;
        }
    }

    public void setPts(int score) {
        if (this.getPts() + score > INITIAL_STATUS) {
            this.pts += score;
        }
        if (score == WA_ZARI || score == (WA_ZARI-YUKO)) {
            if (this.getWaZariCounter() == 1) {
                this.pts -= WA_ZARI*2;
                this.pts += IPPON;
            }
            setWaZariCounter(this.getWaZariCounter() + 1);
        }
    }

    public void resetScore(){
        this.pts = INITIAL_STATUS;
    }

    public void setWaZariCounter(int num) {
        this.waZariCounter = num;
    }

    public void setGroundHoldDuration(int number) {
        this.groundHoldDuration = number;
    }

    public void setGroundHold(boolean b) {
        this.groundHold = b;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public Color getSuitColor() {
        return suitColor;
    }

    public int getPts() {
        return pts;
    }

    public int getGroundHoldDuration() {
        return groundHoldDuration;
    }

    public int getNumOfPenalties() {
        return numOfPenalties;
    }

    public int getWaZariCounter() {
        return waZariCounter;
    }

    public boolean isGroundHold() {
        return groundHold;
    }
}
