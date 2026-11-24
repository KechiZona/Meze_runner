package com.azset.mazerunner.map;
import java.util.Properties;

public class GameMap {
    // 1. Burada map-in ölçülərini saxlayan field-ləri yazacağıq.
    private final int width;
    private final int height;
    private final int tileSize;



    // 2. Burada tile-ları saxlayan 2D array olacaq.
    private final TileType[][] tiles;

    // 3. Constructor

    public GameMap(Properties properties){
        this.width = Integer.parseInt(properties.getProperty("width"));
        this.height = Integer.parseInt(properties.getProperty("height"));
        this.tileSize = Integer.parseInt(properties.getProperty("tileSize"));
        this.tiles = new TileType[height][width];
        boolean[][] assigned = new boolean[height][width];

        int entryCount = 0;
        int exitCount = 0;

        if (width <= 0 || height <= 0 || tileSize <= 0) {
            throw new IllegalArgumentException("Map dimensions must be positive");
        }

        for (String key : properties.stringPropertyNames()) {

            if (key.equals("width") || key.equals("height") || key.equals("tileSize")) {
                continue;
            }

            String[] coords = key.trim().split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            if (x < 0 || x >= width || y < 0 || y >= height) {
                throw new IllegalArgumentException(
                        "Invalid coordinates: x=" + x + ", y=" + y
                );
            }
            TileType tile = TileType.fromCode(
                    Integer.parseInt(properties.getProperty(key).trim())
            );

            this.tiles[y][x] = tile;
            assigned[y][x] = true;


            if (tile == TileType.ENTRY) {
                entryCount++;
                if (entryCount > 1) {
                    throw new IllegalArgumentException("Map must have exactly one entry");
                }
            }

            if (tile == TileType.EXIT) {
                exitCount++;
                if (exitCount > 1) {
                    throw new IllegalArgumentException("Map must have at least one exit");
                }
            }



            if (tile == TileType.ENTRY &&
                    !(x == 0 || x == width - 1 || y == 0 || y == height - 1)) {
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

    // 4. Properties-dən map məlumatlarını oxuyan metodlar

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
            throw new IllegalArgumentException("There is no tile at " + x + " " + y);
        }
        return this.tiles[y][x];
    }



    // 5. Tile məlumatlarını əldə edən getter metodları
}