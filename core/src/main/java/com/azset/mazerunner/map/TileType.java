package com.azset.mazerunner.map;

public enum TileType {

    PATH(0),
    WALL(1),
    ENTRY(2),
    EXIT(3),
    TRAP(4),
    ENEMY(5);

    private final int code;

    TileType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
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

        if (this == WALL) {
            return true;
        }

        if (this == EXIT && !hasKey) {
            return true;
        }

        return false;
    }
}