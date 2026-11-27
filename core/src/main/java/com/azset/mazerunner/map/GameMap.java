
package com.azset.mazerunner.map;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private final int width;
    private final int height;
    private final int tileSize;

    private final List<int[]> keyCoordinates;
    private final List<int[]> lifePickupCoordinates;
    private final List<int[]> enemyCoordinates;

    private final TileType[][] tiles;

    public GameMap(Properties properties) {

        if (Integer.parseInt(properties.getProperty("width")) <= 0
                || Integer.parseInt(properties.getProperty("height")) <= 0
                || Integer.parseInt(properties.getProperty("tileSize")) <= 0) {

            throw new IllegalArgumentException("Map dimensions must be positive");
        }

        this.width = Integer.parseInt(properties.getProperty("width"));
        this.height = Integer.parseInt(properties.getProperty("height"));
        this.tileSize = Integer.parseInt(properties.getProperty("tileSize"));

        this.tiles = new TileType[height][width];

        boolean[][] assigned = new boolean[height][width];

        this.keyCoordinates = new ArrayList<>();
        this.lifePickupCoordinates = new ArrayList<>();
        this.enemyCoordinates = new ArrayList<>();

        int entryCount = 0;
        int exitCount = 0;

        for (String key : properties.stringPropertyNames()) {

            if (key.equals("width") || key.equals("height") || key.equals("tileSize")) {
                continue;
            }

            if (key.equals("key") || key.startsWith("key.")) {

                addObjectCoordinate(
                        key,
                        properties.getProperty(key),
                        keyCoordinates
                );

                continue;
            }

            if (key.equals("lifePickup") || key.startsWith("lifePickup.")) {

                addObjectCoordinate(
                        key,
                        properties.getProperty(key),
                        lifePickupCoordinates
                );

                continue;
            }

            if (key.equals("enemy") || key.startsWith("enemy.")) {

                addObjectCoordinate(
                        key,
                        properties.getProperty(key),
                        enemyCoordinates
                );

                continue;
            }

            String[] coords = key.trim().split(",");

            if (coords.length != 2) {
                throw new IllegalArgumentException("Wrong type of coordinate");
            }

            int x = Integer.parseInt(coords[0].trim());
            int y = Integer.parseInt(coords[1].trim());

            if (x < 0 || x >= width || y < 0 || y >= height) {
                throw new IllegalArgumentException(
                        "Invalid coordinates: x=" + x + ", y=" + y
                );
            }

            TileType tile = TileType.fromCode(
                    Integer.parseInt(properties.getProperty(key).trim())
            );

            if (assigned[y][x]) {
                throw new IllegalArgumentException("Duplicate tile coordinate");
            }

            this.tiles[y][x] = tile;
            assigned[y][x] = true;

            if (tile == TileType.ENTRY) {

                entryCount++;

                if (entryCount > 1) {
                    throw new IllegalArgumentException(
                            "Map must have exactly one entry"
                    );
                }
            }

            if (tile == TileType.EXIT) {

                exitCount++;

                if (exitCount > 1) {
                    throw new IllegalArgumentException(
                            "Map must have exactly one exit"
                    );
                }
            }

            if (tile == TileType.ENTRY
                    && !(x == 0 || x == width - 1 || y == 0 || y == height - 1)) {

                throw new IllegalArgumentException("Entry must be on boundary");
            }
        }

        if (entryCount != 1) {
            throw new IllegalArgumentException("Map must have exactly one entry");
        }

        if (exitCount != 1) {
            throw new IllegalArgumentException("Map must have exactly one exit");
        }

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                if (!assigned[y][x]) {
                    throw new IllegalArgumentException(
                            "Missing tile at: x=" + x + ", y=" + y
                    );
                }
            }
        }
    }

    private void addObjectCoordinate(
            String propertyName,
            String coordinateValue,
            List<int[]> targetList) {

        if (coordinateValue == null || coordinateValue.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing coordinates for: " + propertyName
            );
        }

        String[] coordinates = coordinateValue.trim().split(",", -1);

        if (coordinates.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid coordinates for: " + propertyName
            );
        }

        int x = Integer.parseInt(coordinates[0].trim());
        int y = Integer.parseInt(coordinates[1].trim());

        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(
                    "Object outside map: " + propertyName
            );
        }

        targetList.add(new int[]{x, y});
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getWidth() {
        return width;
    }

    public TileType getTile(int x, int y) {

        if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
            throw new IllegalArgumentException(
                    "There is no tile at " + x + " " + y
            );
        }

        return this.tiles[y][x];
    }

    public int[] getEntryCoordinates() {

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                if (tiles[y][x] == TileType.ENTRY) {
                    return new int[]{x, y};
                }
            }
        }

        throw new IllegalStateException("Map has no ENTRY tile");
    }

    public List<int[]> getEnemyCoordinates() {
        return enemyCoordinates;
    }
}
