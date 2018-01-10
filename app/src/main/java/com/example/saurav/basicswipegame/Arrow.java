package com.example.saurav.basicswipegame;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import static com.example.saurav.basicswipegame.Arrow.Direction.UP;

/**
 * Created by Saurav on 12/15/2017.
 */

public class Arrow {

    public enum Direction { UP, DOWN, LEFT, RIGHT}

    final public Direction dir;
    final public String repChar;

    public Arrow(Direction dir) {
        this.dir = dir;
        repChar = getRepChar(dir);
    }

    public static Direction getDirection(float x1, float y1, float x2, float y2) {
        double rad = Math.atan2(y2-y1, x2-x1);
        if(rad < 0)
            rad+=2*Math.PI;

        if(rad<Math.PI/4 || rad>7*Math.PI/4)
            return Direction.RIGHT;
        else if(rad>Math.PI/4 && rad<3*Math.PI/4)
            return Direction.DOWN;
        else if(rad>3*Math.PI/4 && rad<5*Math.PI/4)
            return Direction.LEFT;
        else return Direction.UP;
    }

    public static String getRepChar(Direction dir) {
        switch (dir) {
            case UP:
                return "A";
            case DOWN:
                return "V";
            case RIGHT:
                return ">";
            case LEFT:
            default:
                return "<";
        }
    }
}
