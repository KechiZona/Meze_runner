package com.azset.mazerunner.controls;

import com.badlogic.gdx.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.IOException;

public class KeyBindingsTest {

    @Test
    public void testDefaultKeys() {
        KeyBindings bindings = new KeyBindings();

        assertEquals(Input.Keys.UP, bindings.getUp());
        assertEquals(Input.Keys.DOWN, bindings.getDown());
        assertEquals(Input.Keys.LEFT, bindings.getLeft());
        assertEquals(Input.Keys.RIGHT, bindings.getRight());
        assertEquals(Input.Keys.SHIFT_LEFT, bindings.getSprint());
        assertEquals(Input.Keys.ESCAPE, bindings.getPause());
    }

    @Test
    public void testRemapping() {
        KeyBindings bindings = new KeyBindings();

        bindings.setUp(Input.Keys.W);

        assertEquals(Input.Keys.W, bindings.getUp());
    }

    @Test
    public void testInvalidKey() {
        KeyBindings bindings = new KeyBindings();

        bindings.setUp(-500);

        assertEquals(Input.Keys.UP, bindings.getUp());
    }

    @Test
    public void testReset() {
        KeyBindings bindings = new KeyBindings();

        bindings.setUp(Input.Keys.W);
        bindings.resetToDefaults();

        assertEquals(Input.Keys.UP, bindings.getUp());
    }

    @Test
    public void testLoad() throws IOException {
        StringReader reader = new StringReader("up=W");

        KeyBindingsLoader loader = new KeyBindingsLoader();
        KeyBindings bindings = loader.load(reader);

        assertEquals(Input.Keys.W, bindings.getUp());
    }

    @Test
    public void testInvalidConfig() throws IOException {
        StringReader reader = new StringReader("up=Banana");

        KeyBindingsLoader loader = new KeyBindingsLoader();
        KeyBindings bindings = loader.load(reader);

        assertEquals(Input.Keys.UP, bindings.getUp());
    }

    @Test
    public void testSave() throws IOException {
        KeyBindings bindings = new KeyBindings();
        bindings.setUp(Input.Keys.W);

        StringWriter writer = new StringWriter();

        KeyBindingsSaver saver = new KeyBindingsSaver();
        saver.save(bindings, writer);

        assertTrue(writer.toString().contains("up=W"));
    }
}
