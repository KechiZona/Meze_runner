
package com.azset.mazerunner.model;

import com.azset.mazerunner.map.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyManager {

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<float[]> targets = new ArrayList<>();

    private final GameMap map;
    private final EnemyAI enemyAI;
    private final Random random = new Random();

    private static final int DETECTION_RANGE = 5;
    private static final int LOSE_RANGE = 7;

    private final int[][] directions = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public EnemyManager(GameMap map) {

        this.map = map;
        this.enemyAI = new EnemyAI(map);

        spawnEnemies();
    }

    private void spawnEnemies() {

        int tileSize = map.getTileSize();

        float enemySize = tileSize * 0.75f;
        float offset = (tileSize - enemySize) / 2f;

        for (int[] coordinates : map.getEnemyCoordinates()) {

            int x = coordinates[0];
            int y = coordinates[1];

            float worldX = x * tileSize + offset;

            float worldY =
                    (map.getHeight() - 1 - y)
                            * tileSize + offset;

            enemies.add(new Enemy(
                    worldX,
                    worldY,
                    enemySize
            ));

            targets.add(new float[]{
                    worldX,
                    worldY
            });
        }
    }

    public void update(float delta, Player player) {

        for (int i = 0; i < enemies.size(); i++) {

            Enemy enemy = enemies.get(i);
            float[] target = targets.get(i);

            updateEnemyState(enemy, player);

            float dx = target[0] - enemy.getX();
            float dy = target[1] - enemy.getY();

            float distance = (float) Math.sqrt(
                    dx * dx + dy * dy
            );

            float step = enemy.getSpeed() * delta;

            if (distance <= step) {

                enemy.setPosition(target[0], target[1]);

                chooseNextTarget(enemy, player, target);

            } else {

                enemy.move(
                        dx / distance,
                        dy / distance,
                        delta
                );
            }
        }
    }

    private void updateEnemyState(
            Enemy enemy, Player player) {

        float dx =
                player.getX() + player.getWidth() / 2f
                        - enemy.getX() - enemy.getWidth() / 2f;

        float dy =
                player.getY() + player.getHeight() / 2f
                        - enemy.getY() - enemy.getHeight() / 2f;

        float distance = (float) Math.sqrt(
                dx * dx + dy * dy
        );

        if (distance <= DETECTION_RANGE
                * map.getTileSize()) {

            enemy.setState(EnemyState.CHASE);

        } else if (distance > LOSE_RANGE
                * map.getTileSize()) {

            enemy.setState(EnemyState.PATROL);
        }
    }

    private void chooseNextTarget(
            Enemy enemy,
            Player player,
            float[] target) {

        int tileSize = map.getTileSize();

        int enemyX = (int) Math.floor(
                (enemy.getX() + enemy.getWidth() / 2f)
                        / tileSize
        );

        int enemyY = map.getHeight() - 1
                - (int) Math.floor(
                (enemy.getY() + enemy.getHeight() / 2f)
                        / tileSize
        );

        int playerX = (int) Math.floor(
                (player.getX() + player.getWidth() / 2f)
                        / tileSize
        );

        int playerY = map.getHeight() - 1
                - (int) Math.floor(
                (player.getY() + player.getHeight() / 2f)
                        / tileSize
        );

        if (enemy.getState() == EnemyState.CHASE) {

            int[] next = enemyAI.findNextStep(
                    enemyX,
                    enemyY,
                    playerX,
                    playerY
            );

            if (next != null) {

                setTarget(enemy, target, next[0], next[1]);

                return;
            }
        }

        List<int[]> available = new ArrayList<>();

        for (int[] direction : directions) {

            int nextX = enemyX + direction[0];
            int nextY = enemyY + direction[1];

            if (enemyAI.isWalkable(nextX, nextY)) {

                available.add(new int[]{
                        nextX,
                        nextY
                });
            }
        }

        if (available.isEmpty()) {
            return;
        }

        int[] next = available.get(
                random.nextInt(available.size())
        );

        setTarget(enemy, target, next[0], next[1]);
    }

    private void setTarget(
            Enemy enemy,
            float[] target,
            int tileX,
            int tileY) {

        int tileSize = map.getTileSize();

        float offset =
                (tileSize - enemy.getWidth()) / 2f;

        target[0] = tileX * tileSize + offset;

        target[1] =
                (map.getHeight() - 1 - tileY)
                        * tileSize + offset;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
