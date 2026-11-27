# Key Remapping — Cavad

## 1. Create and load the controls

In MazeRunnerGame.java, declare a KeyBindings field:

```java
private KeyBindings keyBindings;
```

Inside create(), load the configuration:

```java
try (java.io.Reader reader =
        Gdx.files.internal("controls.properties").reader()) {
    keyBindings = new KeyBindingsLoader().load(reader);
} catch (java.io.IOException e) {
    keyBindings = new KeyBindings();
}
```

Import the required classes:

```java
import com.azset.mazerunner.controls.KeyBindings;
import com.azset.mazerunner.controls.KeyBindingsLoader;
import com.azset.mazerunner.controls.KeyBindingsSaver;
```

## 2. Replace hardcoded movement keys

Replace Input.Keys.W with keyBindings.getUp().

Replace Input.Keys.S with keyBindings.getDown().

Replace Input.Keys.A with keyBindings.getLeft().

Replace Input.Keys.D with keyBindings.getRight().

Use getSprint() for sprint and getPause() for pause.

## 3. Change a key

```java
keyBindings.setUp(Input.Keys.W);
```

## 4. Reset controls

```java
keyBindings.resetToDefaults();
```

## 5. Save the controls

```java
try (java.io.Writer writer =
        Gdx.files.local("user-controls.properties").writer(false)) {
    new KeyBindingsSaver().save(keyBindings, writer);
} catch (java.io.IOException e) {
    Gdx.app.error("Controls", "Could not save controls", e);
}
```

On subsequent launches, load user-controls.properties if it exists; otherwise load the default controls.properties from assets.

## 6. Testing

Run:

```bash
./gradlew core:test
```
