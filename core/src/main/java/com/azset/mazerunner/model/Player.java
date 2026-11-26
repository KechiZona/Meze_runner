package com.azset.mazerunner.model;
import com.azset.mazerunner.collision.BoxCollider;

public class Player extends GameObject {
    private float speed = 200;
    private final BoxCollider hitbox;

    public Player(float x, float y, float size) {
        super(x, y, size, size);

        float hitboxSize = 10f;
        float offset = (size - hitboxSize) / 2f;

        hitbox = new BoxCollider(
                x + offset,
                y + offset,
                hitboxSize,
                hitboxSize
        );
    }

    public BoxCollider getHitbox() {
        return hitbox;
    }

    @Override
    public void update(float delta) {
    }

    public void move(float dx, float dy, float dt) {
        x += dx * speed * dt;
        y += dy * speed * dt;

        hitbox.setPosition(
                x + (width - hitbox.getWidth()) / 2f,
                y + (height - hitbox.getHeight()) / 2f
        );
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        float offsetX = (width - hitbox.getWidth()) / 2f;
        float offsetY = (height - hitbox.getHeight()) / 2f;

        hitbox.setPosition(
                x + offsetX,
                y + offsetY
        );
    }
}
