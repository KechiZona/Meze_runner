package com.azset.mazerunner.controls;
import com.badlogic.gdx.Input;
import java.util.Properties;
import java.io.Writer;
import java.io.IOException;


public class KeyBindingsSaver {
    public void save(KeyBindings bindings, Writer writer)
        throws IOException{

        Properties properties = new Properties();

        properties.setProperty("up", Input.Keys.toString(bindings.getUp()));
        properties.setProperty("down", Input.Keys.toString(bindings.getDown()));
        properties.setProperty("left", Input.Keys.toString(bindings.getLeft()));
        properties.setProperty("right", Input.Keys.toString(bindings.getRight()));
        properties.setProperty("sprint", Input.Keys.toString(bindings.getSprint()));
        properties.setProperty("pause", Input.Keys.toString(bindings.getPause()));

        properties.store(writer, "Game controls");

    }
}
