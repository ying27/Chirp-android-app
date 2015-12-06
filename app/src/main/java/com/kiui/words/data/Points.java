package com.kiui.words.data;

/**
 * Created by ying on 05/04/2015.
 */
public class Points {
    private String user;
    private int points;

    public Points(String user, int points) {
        this.user = user;
        this.points = points;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
