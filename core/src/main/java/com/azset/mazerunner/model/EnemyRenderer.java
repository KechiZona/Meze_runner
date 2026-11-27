package com.azset.mazerunner.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnemyRenderer {

    private final Texture enemyTexture;

    public EnemyRenderer() {

        enemyTexture = new Texture(
                Gdx.files.internal(
                        "maze_runner_sprites/assets/enemy.png"
                )
        );

        enemyTexture.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
        );
    }

    public void render(SpriteBatch batch, EnemyManager enemyManager) {

        for (Enemy enemy : enemyManager.getEnemies()) {

            batch.draw(
                    enemyTexture,
                    enemy.getX(),
                    enemy.getY(),
                    enemy.getWidth(),
                    enemy.getHeight()
            );
        }
    }

    public void dispose() {
        enemyTexture.dispose();
    }
}