package com.azset.mazerunner.map;
import java.util.Properties;

public class GameMap {
    private final int width;
    private final int height;
    private final int tileSize;

    private final TileType[][] tiles;

    public GameMap(Properties properties){

        if(Integer.parseInt(properties.getProperty("width"))<=0 || Integer.parseInt(properties.getProperty("height"))<=0 || Integer.parseInt(properties.getProperty("tileSize")) <= 0){
            throw new IllegalArgumentException("Map dimensions must be positive");
        }

        this.width = Integer.parseInt(properties.getProperty("width"));
        this.height = Integer.parseInt(properties.getProperty("height"));
        this.tileSize = Integer.parseInt(properties.getProperty("tileSize"));
        this.tiles = new TileType[height][width];
        boolean[][] assigned = new boolean[height][width];

        int entryCount = 0;
        int exitCount = 0;


        for (String key : properties.stringPropertyNames()) {

            if (key.equals("width") || key.equals("height") || key.equals("tileSize" ) ) {
                continue;
            }

            String[] coords = key.trim().split(",");
            if (coords.length!=2){
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
                    throw new IllegalArgumentException("Map must have exactly one entry");
                }
            }

            if (tile == TileType.EXIT) {
                exitCount++;
                if (exitCount > 1) {
                    throw new IllegalArgumentException("Map must have exactly one exit");                }
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




}