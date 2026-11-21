package com.azset.mazerunner.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.azset.mazerunner.MazeRunnerGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        new Lwjgl3Application(new MazeRunnerGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Maze Runner");
        configuration.useVsync(true);
        configuration.setForegroundFPS(60);
        configuration.setWindowedMode(1280, 720);
        configuration.setResizable(true);
        return configuration;
    }
}
