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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.InputAdapter;
import com.azset.mazerunner.collision.CollisionSystem;

public class MazeRunnerGame extends Game {
    private ShapeRenderer shapes;
    private OrthographicCamera cam;
    private Player player;
    private SpriteBatch batch;
    private Texture playerTexture;
    private FitViewport viewport;
    private CollisionSystem collisionSystem;


    private MapRenderer mapRenderer;

    private GameMap map;

    private static final float VIEW_WIDTH = 240f;
    private static final float VIEW_HEIGHT = 135f;

    private static final float MIN_ZOOM = 0.75f;
    private static final float MAX_ZOOM = 1.25f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        playerTexture = new Texture(
                Gdx.files.internal(
                        // Player gorunusu.
                        "maze_runner_sprites/assets/player1.png"
                )
        );

        playerTexture.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
        );

        cam = new OrthographicCamera();



        MapLoader loader = new MapLoader();

        // Xerite buradan secilir.
        map = loader.load("maps/level3.properties");

        int[] entry = map.getEntryCoordinates();

        float tileSize = map.getTileSize();
        float playerSize = 12f;

        float startX = entry[0] * tileSize;
        float startY = (map.getHeight() - 1 - entry[1]) * tileSize;

        startX += (tileSize - playerSize) / 2f;
        startY += (tileSize - playerSize) / 2f;

        player = new Player(startX, startY, playerSize);

        collisionSystem = new CollisionSystem(map);

        viewport = new FitViewport(VIEW_WIDTH, VIEW_HEIGHT, cam);

        viewport.update(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(),
                true
        );

        cam.zoom = 1f;

        mapRenderer = new MapRenderer(map, batch);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            collisionSystem.movePlayer(player, 0, 0.6f, dt);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            collisionSystem.movePlayer(player, 0, -0.6f, dt);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            collisionSystem.movePlayer(player, -0.6f, 0, dt);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            collisionSystem.movePlayer(player, 0.6f, 0, dt);
        }


        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1);

        viewport.apply();

        cam.position.set(
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f,
                0
        );

        float mapWidth = map.getWidth() * map.getTileSize();
        float mapHeight = map.getHeight() * map.getTileSize();

        float halfViewWidth = cam.viewportWidth * cam.zoom / 2f;
        float halfViewHeight = cam.viewportHeight * cam.zoom / 2f;

        if (mapWidth <= halfViewWidth * 2) {
            cam.position.x = mapWidth / 2f;
        } else {
            cam.position.x = MathUtils.clamp(
                    cam.position.x,
                    halfViewWidth,
                    mapWidth - halfViewWidth
            );
        }

        if (mapHeight <= halfViewHeight * 2) {
            cam.position.y = mapHeight / 2f;
        } else {
            cam.position.y = MathUtils.clamp(
                    cam.position.y,
                    halfViewHeight,
                    mapHeight - halfViewHeight
            );
        }

        cam.update();

        batch.setProjectionMatrix(cam.combined);
        mapRenderer.render();

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean scrolled(float amountX, float amountY) {

                cam.zoom += amountY * 0.1f;

                cam.zoom = MathUtils.clamp(
                        cam.zoom,
                        MIN_ZOOM,
                        MAX_ZOOM
                );

                return true;
            }
        });

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
