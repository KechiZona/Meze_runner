package com.azset.mazerunner.model;

import com.azset.mazerunner.map.GameMap;

import java.util.ArrayDeque;
import java.util.Queue;

public class EnemyAI {

    private final GameMap map;

    private final int[][] directions = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public EnemyAI(GameMap map) {
        this.map = map;
    }

    public boolean isWalkable(int x, int y) {

        if (x < 0 || x >= map.getWidth()
                || y < 0 || y >= map.getHeight()) {
            return false;
        }

        return !map.getTile(x, y).blocksMovement(false);
    }

    public int[] findNextStep(
            int startX, int startY,
            int targetX, int targetY) {

        if (!isWalkable(startX, startY)
                || !isWalkable(targetX, targetY)) {
            return null;
        }

        int width = map.getWidth();
        int height = map.getHeight();

        boolean[][] visited = new boolean[height][width];

        int[][][] parent = new int[height][width][2];

        Queue<int[]> queue = new ArrayDeque<>();

        queue.add(new int[]{startX, startY});

        visited[startY][startX] = true;

        while (!queue.isEmpty()) {

            int[] current = queue.poll();

            int x = current[0];
            int y = current[1];

            if (x == targetX && y == targetY) {

                if (x == startX && y == startY) {
                    return new int[]{x, y};
                }

                while (true) {

                    int previousX = parent[y][x][0];
                    int previousY = parent[y][x][1];

                    if (previousX == startX
                            && previousY == startY) {

                        return new int[]{x, y};
                    }

                    x = previousX;
                    y = previousY;
                }
            }

            for (int[] direction : directions) {

                int nextX = x + direction[0];
                int nextY = y + direction[1];

                if (isWalkable(nextX, nextY)
                        && !visited[nextY][nextX]) {

                    visited[nextY][nextX] = true;

                    parent[nextY][nextX][0] = x;
                    parent[nextY][nextX][1] = y;

                    queue.add(new int[]{nextX, nextY});
                }
            }
        }

        return null;
    }
}