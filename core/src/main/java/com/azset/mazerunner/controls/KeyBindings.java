package com.azset.mazerunner.controls;

import com.badlogic.gdx.Input;

public class KeyBindings {
    private int up = Input.Keys.UP;
    private int down = Input.Keys.DOWN;
    private int left = Input.Keys.LEFT;
    private int right = Input.Keys.RIGHT;
    private int sprint = Input.Keys.SHIFT_LEFT;
    private int pause = Input.Keys.ESCAPE;

    private boolean isValidKey(int key) {
        return key>0
                && key <= 255
                && Input.Keys.toString(key) != null;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getSprint() {
        return sprint;
    }

    public int getPause() {
        return pause;
    }

    public void setDown(int key) {
        if (isValidKey(key)) {
            down = key;
        } else {
            down = Input.Keys.DOWN;
        }
    }

    public void setUp(int key) {
        if (isValidKey(key)) {
            up = key;
        } else {
            up = Input.Keys.UP;
        }
    }

    public void setLeft(int key) {
        if (isValidKey(key)) {
            left = key;
        } else {
            left = Input.Keys.LEFT;
        }
    }

    public void setRight(int key) {
        if (isValidKey(key)) {
            right = key;
        } else {
            right = Input.Keys.RIGHT;
        }
    }

    public void setSprint(int key) {
        if (isValidKey(key)) {
            sprint = key;
        } else {
            sprint = Input.Keys.SHIFT_LEFT;
        }
    }

    public void setPause(int key) {
        if (isValidKey(key)) {
            pause = key;
        } else {
            pause = Input.Keys.ESCAPE;
        }
    }

    public void resetToDefaults() {
        up = Input.Keys.UP;
        down = Input.Keys.DOWN;
        left = Input.Keys.LEFT;
        right = Input.Keys.RIGHT;
        pause = Input.Keys.ESCAPE;
        sprint = Input.Keys.SHIFT_LEFT;

    }

}
