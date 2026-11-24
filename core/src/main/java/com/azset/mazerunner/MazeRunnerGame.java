package com.azset.mazerunner;

import com.azset.mazerunner.model.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class MazeRunnerGame extends Game {
    private ShapeRenderer shapes;
    private OrthographicCamera cam;
    private Player player;
    private SpriteBatch batch;
    private Texture playerTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();

        playerTexture = new Texture(
                Gdx.files.internal(
                        "maze_runner_sprites/assets/player.png"
                )
        );

        playerTexture.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
        );

        cam = new OrthographicCamera();
        cam.setToOrtho(false);

        player = new Player(30, 30, 32);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.move(0, 1, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.move(0, -1, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.move(-1, 0, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.move(1, 0, dt);
        }


        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        cam.update();

        batch.setProjectionMatrix(cam.combined);

        batch.begin();

        batch.draw(
                playerTexture,
                player.getX(),
                player.getY(),
                player.getWidth(),
                player.getHeight()
        );

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
    }
}
