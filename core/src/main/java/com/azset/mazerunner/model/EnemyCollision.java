
package com.azset.mazerunner.model;

import com.azset.mazerunner.collision.BoxCollider;

public class EnemyCollision {

    public static boolean isColliding(
            Player player,
            Enemy enemy) {

        BoxCollider playerCollider = player.getHitbox();
        BoxCollider enemyCollider = enemy.getHitbox();

        return playerCollider.overlaps(enemyCollider);
    }

    public static void checkCollisions(
            Player player,
            EnemyManager enemyManager) {

        for (Enemy enemy : enemyManager.getEnemies()) {

            if (isColliding(player, enemy)) {

                player.takeDamage(1);

                break;
            }
        }
    }
}
