package com.azset.mazerunner.model;

public class Player extends GameObject {
    private float speed = 200;

    public Player(float x, float y, float size) {
        super(x, y, size, size);
    }

    @Override
    public void update(float delta) {
    }

    public void move(float dx, float dy, float dt) {
        x += dx * speed * dt;
        y += dy * speed * dt;
    }
}
