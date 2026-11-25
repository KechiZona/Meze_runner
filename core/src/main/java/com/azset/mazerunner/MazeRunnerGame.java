package com.azset.mazerunner;

import com.azset.mazerunner.map.GameMap;
import com.azset.mazerunner.map.MapLoader;
import com.azset.mazerunner.map.MapRenderer;
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
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MazeRunnerGame extends Game {
    private ShapeRenderer shapes;
    private OrthographicCamera cam;
    private Player player;
    private SpriteBatch batch;
    private Texture playerTexture;
    private FitViewport viewport;

    private MapRenderer mapRenderer;

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


        player = new Player(0, 112, 16);


        MapLoader loader = new MapLoader();

        GameMap map = loader.load("maps/level1.properties");

        float worldWidth = map.getWidth() * map.getTileSize();
        float worldHeight = map.getHeight() * map.getTileSize();

        viewport = new FitViewport(worldWidth, worldHeight, cam);

        viewport.update(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                true
        );

        mapRenderer = new MapRenderer(map, batch);

        mapRenderer = new MapRenderer(map, batch);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.move(0, 0.6f, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.move(0, -0.6f, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.move(-0.6f, 0, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.move(0.6f, 0, dt);
        }


        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        cam.update();

        batch.setProjectionMatrix(cam.combined);
        mapRenderer.render();

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
        viewport.update(width, height, true);
    }
    @Override
    public void dispose() {
        mapRenderer.dispose();
        batch.dispose();
        playerTexture.dispose();
    }
}
