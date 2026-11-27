package com.azset.mazerunner.map;

public enum TileType {

    PATH(0, "tile_floor.png", false),
    WALL(1, "tile_wall.png", true),
    ENTRY(2, "tile_entry.png", false),
    EXIT(3, "tile_exit_locked.png", false),
    TRAP(4, "trap.png", false),
    ENEMY(5, "enemy.png", false),

    PATH_02(6, "tile_floor_02.png", false),
    PATH_03(7, "tile_floor_03.png", false),
    PATH_04(8, "tile_floor_04.png", false),
    PATH_05(9, "tile_floor_05.png", false),
    PATH_06(10, "tile_floor_06.png", false),
    PATH_07(11, "tile_floor_07.png", false),
    PATH_08(12, "tile_floor_08.png", false),
    PATH_09(13, "tile_floor_09.png", false),
    PATH_10(14, "tile_floor_10.png", false),
    PATH_11(15, "tile_floor_11.png", false),
    PATH_12(16, "tile_floor_12.png", false),

    WALL_CAP(17, "tile_wall_cap.png", true),
    WALL_CPU(18, "tile_wall_cpu.png", true),
    WALL_HEATSINK(19, "tile_wall_heatsink.png", true),
    WALL_HEATSINK_HORIZONTAL(20, "tile_wall_heatsink(horizotal).png", true),
    WALL_SLOT(21, "tile_wall_slot.png", true),
    WALL_VERTICAL(22, "tile_wall(vertical).png", true);

    private final int code;
    private final String assetName;
    private final boolean wall;

    TileType(int code, String assetName, boolean wall) {
        this.code = code;
        this.assetName = assetName;
        this.wall = wall;
    }

    public int getCode() {
        return code;
    }

    public String getAssetName() {
        return assetName;
    }

    public boolean isWall() {
        return wall;
    }

    public static TileType fromCode(int code) {

        for (TileType tile : values()) {

            if (tile.code == code) {
                return tile;
            }
        }

        throw new IllegalArgumentException(
                "Invalid tile code: " + code
        );
    }

    public boolean blocksMovement(boolean hasKey) {

        if (wall) {
            return true;
        }

        if (this == EXIT && !hasKey) {
            return true;
        }

        return false;
    }
}