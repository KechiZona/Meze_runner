
package com.azset.mazerunner.model;

import com.azset.mazerunner.collision.BoxCollider;

public class Player extends GameObject {

    private float speed = 200f;
    private final BoxCollider hitbox;

    private int health = 3;
    private float damageCooldown = 0f;

    private static final float DAMAGE_COOLDOWN = 2f;

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

        if (damageCooldown > 0) {
            damageCooldown = Math.max(
                    0f,
                    damageCooldown - delta
            );
        }
    }

    public void move(float dx, float dy, float dt) {

        if (isDead()) {
            return;
        }

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

    public void takeDamage(int damage) {

        if (damageCooldown > 0 || isDead() || damage <= 0) {
            return;
        }

        health = Math.max(0, health - damage);

        damageCooldown = DAMAGE_COOLDOWN;

        System.out.println("Player health: " + health);

        if (isDead()) {
            System.out.println("GAME OVER");
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean isInvulnerable() {
        return damageCooldown > 0;
    }
}
