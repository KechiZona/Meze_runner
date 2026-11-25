
package com.azset.mazerunner.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class MapRenderer implements Disposable {

    private final GameMap map;
    private final SpriteBatch batch;

    private final Texture floorTexture;
    private final Texture wallTexture;
    private final Texture entryTexture;
    private final Texture exitTexture;
    private final Texture trapTexture;
    private final Texture enemyTexture;

    public MapRenderer(GameMap map, SpriteBatch batch) {

        this.map = map;
        this.batch = batch;

        floorTexture = new Texture(Gdx.files.internal(
                "maze_runner_sprites/assets/tile_floor.png"));

        wallTexture = new Texture(Gdx.files.internal(
                "maze_runner_sprites/assets/tile_wall.png"));

        entryTexture = new Texture(Gdx.files.internal(
                "maze_runner_sprites/assets/tile_entry.png"));

        exitTexture = new Texture(Gdx.files.internal(
                "maze_runner_sprites/assets/tile_exit_locked.png"));

        trapTexture = new Texture(Gdx.files.internal(
                "maze_runner_sprites/assets/trap.png"));

        enemyTexture = new Texture(Gdx.files.internal(
                "maze_runner_sprites/assets/enemy.png"));
    }

    public void render() {

        batch.begin();

        for (int y = 0; y < map.getHeight(); y++) {

            for (int x = 0; x < map.getWidth(); x++) {

                TileType tile = map.getTile(x, y);

                float drawX = x * map.getTileSize();

                float drawY = (map.getHeight() - 1 - y)
                        * map.getTileSize();

                Texture texture = switch (tile) {
                    case PATH -> floorTexture;
                    case WALL -> wallTexture;
                    case ENTRY -> entryTexture;
                    case EXIT -> exitTexture;
                    case TRAP -> trapTexture;
                    case ENEMY -> enemyTexture;
                };
                batch.draw(
                        texture,
                        drawX,
                        drawY,
                        map.getTileSize(),
                        map.getTileSize()
                );
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {

        floorTexture.dispose();
        wallTexture.dispose();
        entryTexture.dispose();
        exitTexture.dispose();
        trapTexture.dispose();
        enemyTexture.dispose();
    }
}
