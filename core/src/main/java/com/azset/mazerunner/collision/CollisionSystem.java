package com.azset.mazerunner.collision;

import com.azset.mazerunner.map.GameMap;
import com.azset.mazerunner.map.TileType;
import com.azset.mazerunner.model.Player;

import com.azset.mazerunner.collision.BoxCollider;

public class CollisionSystem {

    private final GameMap map;

    public CollisionSystem(GameMap map) {
        this.map = map;
    }

    public boolean isColliding(Player p) {

        BoxCollider hitbox = p.getHitbox();

        int tileSize = map.getTileSize();

        float left = hitbox.getX();
        float right = hitbox.getX() + hitbox.getWidth();

        float bottom = hitbox.getY();
        float top = hitbox.getY() + hitbox.getHeight();

        int minTileX = (int) Math.floor(left / tileSize);
        int maxTileX = (int) Math.floor((right - 0.0001f) / tileSize);

        int minTileY = (int) Math.floor(bottom / tileSize);
        int maxTileY = (int) Math.floor((top - 0.0001f) / tileSize);

        for (int y = minTileY; y <= maxTileY; y++) {

            for (int x = minTileX; x <= maxTileX; x++) {

                if (x < 0 || x >= map.getWidth() ||
                        y < 0 || y >= map.getHeight()) {

                    return true;
                }
                int mapY = map.getHeight() - 1 - y;

                if (map.getTile(x, mapY) == TileType.WALL) {
                    return true;
                }


            }
        }

        return false;
    }

    public void movePlayer(Player p, float dx, float dy, float dt) {

        float oldX = p.getX();
        float oldY = p.getY();

        p.move(dx, 0, dt);
        if (isColliding(p)) {
            p.setPosition(oldX, p.getY());
        }
        p.move(0, dy, dt);
        if (isColliding(p)) {
            p.setPosition(p.getX(), oldY);
        }


    }

}