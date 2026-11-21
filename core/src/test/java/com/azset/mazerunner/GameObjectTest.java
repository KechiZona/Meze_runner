package com.azset.mazerunner;

import com.azset.mazerunner.model.GameObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameObjectTest {
    @Test
    void testInitialPosition() {
        GameObject obj = new GameObject(10f, 20f, 32f, 32f) {
            @Override
            public void update(float delta) {
            }
        };
        assertEquals(10f, obj.getX());
        assertEquals(20f, obj.getY());
    }
}
