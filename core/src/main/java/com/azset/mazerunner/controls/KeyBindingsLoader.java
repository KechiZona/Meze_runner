
package com.azset.mazerunner.controls;

import java.util.Properties;
import java.io.Reader;
import java.io.IOException;
import com.badlogic.gdx.Input;

public class KeyBindingsLoader {

    public KeyBindings load(Reader reader) throws IOException {

        Properties properties = new Properties();
        properties.load(reader);

        KeyBindings bindings = new KeyBindings();

        bindings.setUp(readKey(properties, "up"));
        bindings.setDown(readKey(properties, "down"));
        bindings.setLeft(readKey(properties, "left"));
        bindings.setRight(readKey(properties, "right"));
        bindings.setSprint(readKey(properties, "sprint"));
        bindings.setPause(readKey(properties, "pause"));

        return bindings;
    }

    private int readKey(Properties properties, String action) {

        String value = properties.getProperty(action);

        if (value == null) {
            return -1;
        }

        return Input.Keys.valueOf(value.trim());
    }
}
