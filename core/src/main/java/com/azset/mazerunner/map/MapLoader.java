package com.azset.mazerunner.map;

import com.badlogic.gdx.Gdx;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MapLoader {

    public GameMap load(String path) {

        Properties properties = new Properties();

        try (InputStream input = Gdx.files.internal(path).read()) {

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load map: " + path, e);
        }

        return new GameMap(properties);
    }
}