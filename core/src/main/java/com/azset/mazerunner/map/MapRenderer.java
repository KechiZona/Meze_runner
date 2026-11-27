
package com.azset.mazerunner.map;
import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class MapRenderer implements Disposable {

    private final GameMap map;
    private final SpriteBatch batch;

    private final Map<TileType, Texture> textures =
            new EnumMap<>(TileType.class);



    public MapRenderer(GameMap map, SpriteBatch batch) {

        this.map = map;
        this.batch = batch;

        for (TileType tile : TileType.values()) {

            Texture texture = new Texture(
                    Gdx.files.internal(
                            "maze_runner_sprites/assets/" + tile.getAssetName()
                    )
            );

            textures.put(tile, texture);
        }
    }

    public void render() {

        batch.begin();

        for (int y = 0; y < map.getHeight(); y++) {

            for (int x = 0; x < map.getWidth(); x++) {

                TileType tile = map.getTile(x, y);

                float drawX = x * map.getTileSize();

                float drawY = (map.getHeight() - 1 - y)
                        * map.getTileSize();

                Texture texture = textures.get(tile);

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

        for (Texture texture : textures.values()) {
            texture.dispose();
        }
    }
}
