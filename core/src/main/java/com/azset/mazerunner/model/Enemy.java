
package com.azset.mazerunner.model;

import com.azset.mazerunner.collision.BoxCollider;

public class Enemy extends GameObject {

    private float speed = 60f;

    private EnemyState state = EnemyState.PATROL;

    private final BoxCollider hitbox;

    public Enemy(float x, float y, float size) {
        super(x, y, size, size);

        float hitboxSize = size * 0.7f;
        float offset = (size - hitboxSize) / 2f;

        this.hitbox = new BoxCollider(
                x + offset,
                y + offset,
                hitboxSize,
                hitboxSize
        );
    }

    @Override
    public void update(float delta) {

    }

    public void move(float dx, float dy, float dt) {
        x += dx * getSpeed() * dt;
        y += dy * getSpeed() * dt;

        updateHitbox();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;

        updateHitbox();
    }

    private void updateHitbox() {
        float hitboxSize = width * 0.7f;
        float offset = (width - hitboxSize) / 2f;

        hitbox.setPosition(x + offset, y + offset);
    }

    public BoxCollider getHitbox() {
        return hitbox;
    }

    public float getSpeed() {

        if (state == EnemyState.CHASE) {
            return speed * 1.4f;
        }

        return speed;
    }

    public EnemyState getState() {
        return state;
    }

    public void setState(EnemyState state) {
        this.state = state;
    }
}
