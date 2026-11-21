package com.azset.mazerunner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;

public class MazeRunnerGame extends Game {
    @Override
    public void create() {
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.12f, 0.16f, 1f);
        super.render();
    }
}
