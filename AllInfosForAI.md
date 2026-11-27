# AllInfosForAI — Maze Runner Project Context

> **Purpose of this file:** Give an AI coding assistant or a new teammate enough reliable context to understand the current Maze Runner repository, its architecture, conventions, existing systems, and known gaps before changing code.
>
> **Repository:** [KechiZona/Meze_runner](https://github.com/KechiZona/Meze_runner)  
> **Primary branch:** `main`  
> **Last context snapshot:** 27 November 2026. Repository code and branch state can change; always verify the current checkout before acting.

---

## 0. Instructions for any AI working on this repository

Treat these rules as project requirements, not optional style preferences.

1. **Preserve the existing core architecture.** The current map, game-object, player, collision, rendering, and game-loop systems are the foundation. New features must be integrated into those systems rather than replacing them or creating parallel systems unnecessarily.
2. **Inspect before editing.** Read the current version of every relevant file and check `git status`, the active branch, and recent commits before proposing or applying changes. This document is context, not a substitute for the actual source code.
3. **Make the smallest complete change.** Do not refactor unrelated classes, rename packages, reformat whole files, or change working behaviour for cosmetic reasons. Avoid broad rewrites.
4. **Do not invent functionality.** Distinguish clearly between implemented, partially implemented, and planned features. In particular, map metadata parsing does not mean a collectible or trap gameplay loop is finished.
5. **Preserve tile IDs.** Do not casually renumber `TileType` codes. Map files store numeric IDs, so changing an ID can silently change or break existing maps.
6. **Keep map-coordinate conventions consistent.** Properties-file map coordinates use a top-left row origin; rendered world coordinates use the usual bottom-left origin. Apply the existing Y conversion when moving between them.
7. **Reuse existing collision code.** `BoxCollider` and `CollisionSystem` already exist. Do not introduce another general-purpose AABB/collision framework for a feature that can use them.
8. **Keep objects separate from terrain tiles where appropriate.** Enemies are now object metadata (`enemy=...`) placed over walkable map tiles, not a `TileType.ENEMY` terrain tile. Follow this design for future placeable entities unless there is a strong, reviewed reason to change it.
9. **Verify after each meaningful change.** At minimum run `./gradlew :core:compileJava`; for runtime changes also run `./gradlew lwjgl3:run` and test the affected behaviour. Use `git diff --check` before committing.
10. **Do not commit or push unless the human explicitly asks.** Stage explicit files rather than blindly running `git add .`. Never discard another contributor's changes.
11. **Explain changes after implementing them.** The team is learning the codebase. Explain what changed, why, which existing system it uses, and how it was verified.
12. **If uncertain, ask for the current file or inspect it.** Do not reconstruct a whole class from an old chat excerpt when the repository may have moved on.

### Safe AI task-start checklist

```bash
git status --short
git branch --show-current
git log -5 --oneline
```

Then inspect the relevant source and the current branch's version of this document. Never assume that `main`, a collaborator branch, or the public GitHub view is identical to the developer's local working tree.

---

## 1. Project identity and current scope

**Maze Runner** is a 2D, tile/grid-based maze game implemented in Java with the libGDX framework. The current runnable target is the desktop LWJGL3 backend.

The project currently has a working foundation for:

- loading a maze from a Java `.properties` file;
- representing terrain with numeric tile IDs and a `TileType` enum;
- drawing the tile grid with textures;
- creating and moving a player object;
- preventing the player from moving through wall tiles;
- tracking the camera and supporting mouse-wheel zoom;
- spawning enemies from map object metadata;
- enemy Patrol/Chase states and BFS-based pathfinding;
- player/enemy overlap checks using the shared `BoxCollider` class;
- player health and temporary damage cooldown.

The current startup code selects **`maps/level3.properties`**. The game is not yet a full screen/menu-based game: the current central runtime is `MazeRunnerGame`, and the level is selected in code.

### Current integration snapshot

The working history shared by the team shows the following relevant commits:

- `3445e76` — `feat(map): add unique tile IDs, dynamic texture loading, and wall collision support`
- `82d8d73` — `Integrate enemy AI and collision system`
- `f6449f7` — `Merge amil into main`

The user's terminal confirmed the local `main` merge succeeded with a clean working tree. The GitHub repository view later showed `main` containing the merge. Commit/remote state must still be checked live before doing Git operations.

---

## 2. Technology stack and build configuration

| Item | Current repository configuration |
|---|---|
| Language | Java |
| Java source/target compatibility | 17 |
| Game framework | libGDX |
| libGDX version | `1.12.1` |
| Desktop backend | LWJGL3 |
| Build system | Gradle Wrapper |
| Gradle wrapper distribution | Gradle `8.5` |
| Test framework dependency | JUnit Jupiter `5.10.2` |
| Root project name | `MazeRunner` |
| Gradle modules | `core`, `lwjgl3` |
| Desktop main class | `com.azset.mazerunner.lwjgl3.Lwjgl3Launcher` |

Relevant configuration files:

- `settings.gradle` — includes `core` and `lwjgl3`.
- `build.gradle` — applies Java library configuration to subprojects, sets Java 17 compatibility and UTF-8 compilation.
- `gradle.properties` — contains Gradle memory settings, dependency versions, and currently a Linux-specific Java home.
- `gradle/wrapper/gradle-wrapper.properties` — points to Gradle 8.5.
- `core/build.gradle` — libGDX core dependency and JUnit 5 test dependencies.
- `lwjgl3/build.gradle` — desktop application plugin, LWJGL3 backend, desktop native dependency, assets resource root, and JAR packaging.

### Important machine-specific configuration warning

The current `gradle.properties` includes:

```properties
org.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64
```

That is a path from one Linux environment, not a portable project path. On Windows, macOS, or a Linux machine with Java installed elsewhere, Gradle may fail to start with this setting. A teammate should use JDK 17 and, if necessary, update or remove only this machine-specific line locally. Do not casually commit an individual developer's local JDK path for the whole team.

---

## 3. Repository layout

The main structure is:

```text
Meze_runner/
├── assets/
│   ├── maps/
│   │   ├── level1.properties
│   │   ├── level2.properties
│   │   └── level3.properties
│   └── maze_runner_sprites/
│       ├── assets/                 # Runtime texture set used by current code
│       ├── assets_pixel/           # Alternate pixel-art set
│       ├── assets_pixel_v2/        # Alternate pixel-art set
│       ├── assets_pixel.zip
│       └── assets_pixel_v2.zip
├── core/
│   ├── build.gradle
│   └── src/main/java/com/azset/mazerunner/
│       ├── MazeRunnerGame.java
│       ├── collision/
│       │   ├── BoxCollider.java
│       │   └── CollisionSystem.java
│       ├── map/
│       │   ├── GameMap.java
│       │   ├── MapLoader.java
│       │   ├── MapRenderer.java
│       │   └── TileType.java
│       └── model/
│           ├── GameObject.java
│           ├── Player.java
│           ├── Enemy.java
│           ├── EnemyAI.java
│           ├── EnemyCollision.java
│           ├── EnemyManager.java
│           ├── EnemyRenderer.java
│           └── EnemyState.java
├── lwjgl3/
│   ├── build.gradle
│   └── src/main/java/com/azset/mazerunner/lwjgl3/
│       └── Lwjgl3Launcher.java
├── gradle/wrapper/
├── .gitignore
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── plan.docx
└── libresprite-v1.3-linux-x86_64.AppImage
```

Build output such as `core/build/` and `lwjgl3/build/` is generated. Do not hand-edit or commit build output.

### Package responsibilities

- `com.azset.mazerunner` — main game class and top-level runtime orchestration.
- `com.azset.mazerunner.map` — map parsing, tile definitions, map data, tile rendering.
- `com.azset.mazerunner.model` — world/game objects and enemy logic.
- `com.azset.mazerunner.collision` — reusable collider and player-vs-map movement collision.
- `com.azset.mazerunner.lwjgl3` — desktop launch configuration only.

Keep platform-specific desktop setup out of the core gameplay classes.

---

## 4. Runtime architecture and frame flow

### 4.1 Startup

`Lwjgl3Launcher.main()` creates a `Lwjgl3Application` and passes it a new `MazeRunnerGame` instance. The desktop window is titled `Maze Runner`, uses VSync, targets 60 FPS, opens at 1280×720, and is resizable.

`MazeRunnerGame.create()` currently performs this setup:

1. Creates the shared `SpriteBatch`.
2. Loads the player texture from `maze_runner_sprites/assets/player1.png` and sets `Nearest` filtering.
3. Creates an `OrthographicCamera`.
4. Loads the selected map through `MapLoader` using `maps/level3.properties`.
5. Gets the map's `ENTRY` tile and converts its tile coordinates into world coordinates.
6. Creates the `Player` with a 12×12 world-space sprite size, centered in the entry tile.
7. Creates `CollisionSystem` with the loaded `GameMap`.
8. Creates a `FitViewport` with a virtual size of 240×135.
9. Creates `MapRenderer`.
10. Creates `EnemyManager` and `EnemyRenderer`.
11. Creates a `BitmapFont` with scale 0.7 (the current shown render code does not draw it as a HUD).

### 4.2 Per-frame `render()` sequence

The current frame flow is:

1. Read `deltaTime` from `Gdx.graphics.getDeltaTime()`.
2. While the player is alive:
   - update Player timers;
   - process WASD input through `CollisionSystem.movePlayer()`;
   - update enemies through `EnemyManager.update()`;
   - check Player/Enemy overlap through `EnemyCollision.checkCollisions()`.
3. Clear the screen.
4. Apply the viewport and position the camera around the player.
5. Clamp camera position to map bounds.
6. Update the camera and set the SpriteBatch projection matrix.
7. Render the map tiles.
8. Process mouse-wheel zoom, clamped between 0.75 and 1.25.
9. Begin the shared SpriteBatch, render enemies, draw the player, and end the batch.

The map renderer starts and ends the batch internally; enemy/player rendering happens afterward in its own batch interval. Preserve this begin/end ordering unless deliberately redesigning the renderer contract.

### 4.3 Disposal

`MazeRunnerGame.dispose()` disposes the map renderer, shared batch, player texture, enemy renderer, and font. Any new Texture, SpriteBatch, font, shape renderer, audio object, or other disposable resource must have a clear owner and must be disposed exactly once.

### 4.4 Camera and controls

- Virtual viewport: `240f × 135f`.
- Zoom limits: `0.75f` to `1.25f`.
- Mouse-wheel scrolling changes zoom by `0.1f` per scroll amount.
- Player movement keys currently handled: `W`, `A`, `S`, `D`.
- The code currently installs an `InputAdapter` from inside `render()`; this is a known place that may later be improved, but do not combine that cleanup with unrelated feature work.

---

## 5. Map format and coordinate system

### 5.1 Resource path

`lwjgl3/build.gradle` sets the assets folder as the desktop resource root:

```groovy
sourceSets.main.resources.srcDirs = [rootProject.file('assets').path]
```

Therefore, this runtime path:

```java
loader.load("maps/level3.properties");
```

resolves to the repository file:

```text
assets/maps/level3.properties
```

Similarly, runtime sprite path `maze_runner_sprites/assets/enemy.png` resolves to:

```text
assets/maze_runner_sprites/assets/enemy.png
```

Do not add the literal `assets/` prefix to a `Gdx.files.internal()` path while the assets directory is the resource root.

### 5.2 Map file structure

Maps use Java `.properties` syntax. At minimum, each map defines:

```properties
width=25
height=20
tileSize=16

# Each remaining terrain property uses "x,y=tileCode".
0,0=18
1,0=19
2,0=19
```

- `width` = number of columns.
- `height` = number of rows.
- `tileSize` = tile width/height in world units (current maps use 16).
- `x,y` is the tile coordinate.
- Tile properties must cover every coordinate exactly once; missing or duplicate terrain tiles are rejected.
- The map must contain exactly one `ENTRY` and one `EXIT` tile.
- The `ENTRY` tile must be on the map boundary.
- Terrain coordinate values must be valid `TileType` numeric IDs.

The current maps found in the repository are:

| File | Width | Height | Tile size | Notes |
|---|---:|---:|---:|---|
| `level1.properties` | 21 | 15 | 16 | Contains legacy tile code `5` entries; see known issue below. |
| `level2.properties` | 30 | 38 | 16 | Generated map file; inspect IDs before changing tile definitions. |
| `level3.properties` | 25 | 20 | 16 | Currently selected by `MazeRunnerGame`; contains three Enemy object properties. |

### 5.3 Tile-grid Y versus world-space Y

This is one of the most important project conventions.

- In `.properties`, map row `y=0` is the **top** row.
- In libGDX world coordinates, `worldY=0` is the **bottom** of the world.
- Rendering, spawning, and world-to-map conversion must invert Y consistently.

Map tile `(tileX, tileY)` to world-space bottom-left position:

```java
float worldX = tileX * tileSize;
float worldY = (mapHeight - 1 - tileY) * tileSize;
```

For an object centered inside a tile, add the appropriate size offset. Enemy spawning already uses this convention. `CollisionSystem` converts world-space tile rows back into map rows with:

```java
int mapY = map.getHeight() - 1 - worldTileY;
```

Do not remove or reverse this conversion without changing every related path and testing map rendering, player collision, and enemy navigation.

### 5.4 Placeable object metadata

`GameMap` supports special object-coordinate property names separately from terrain tile properties:

```properties
# Key metadata
key=15,7
key.2=...

# Life-pickup metadata
lifePickup=...
lifePickup.2=...

# Enemy metadata
enemy=9,3
enemy.2=12,11
enemy.3=12,18
```

Rules:

- `key` or `key.*` is parsed into `keyCoordinates`.
- `lifePickup` or `lifePickup.*` is parsed into `lifePickupCoordinates`.
- `enemy` or `enemy.*` is parsed into `enemyCoordinates`.
- Each value must contain exactly two comma-separated integer coordinates.
- The coordinates must be inside map bounds.
- These metadata keys are not terrain tiles and must not be parsed as `x,y=tileCode` entries.
- Unknown property names are not general-purpose metadata: the current parser attempts to interpret other non-header keys as tile coordinates and can reject them.
- Current validation checks that object coordinates are in bounds, but does not validate that an object's tile is walkable or that two objects do not overlap.

**Important implementation status:** Enemy metadata has a getter used by the EnemyManager. Key and life-pickup coordinates are parsed, but the current `GameMap` source does not show corresponding public getters or complete runtime collection/rendering logic. Do not describe keys or life pickups as fully playable features yet.

### 5.5 Current Level 3 object metadata

The current `level3.properties` contains:

```properties
key=15,7

enemy=9,3
enemy.2=12,11
enemy.3=12,18
```

These are **object coordinates**, not tile IDs. The three enemies are placed over terrain tiles; they are not `TileType.ENEMY` entries.

---

## 6. TileType reference

`TileType` maps stable numeric IDs to a texture filename and a wall flag.

| Numeric ID | Enum | Runtime texture filename | Wall? |
|---:|---|---|---|
| 0 | `PATH` | `tile_floor.png` | No |
| 1 | `WALL` | `tile_wall.png` | Yes |
| 2 | `ENTRY` | `tile_entry.png` | No |
| 3 | `EXIT` | `tile_exit_locked.png` | No* |
| 4 | `TRAP` | `trap.png` | No |
| 5 | **Removed / reserved** | — | — |
| 6 | `PATH_02` | `tile_floor_02.png` | No |
| 7 | `PATH_03` | `tile_floor_03.png` | No |
| 8 | `PATH_04` | `tile_floor_04.png` | No |
| 9 | `PATH_05` | `tile_floor_05.png` | No |
| 10 | `PATH_06` | `tile_floor_06.png` | No |
| 11 | `PATH_07` | `tile_floor_07.png` | No |
| 12 | `PATH_08` | `tile_floor_08.png` | No |
| 13 | `PATH_09` | `tile_floor_09.png` | No |
| 14 | `PATH_10` | `tile_floor_10.png` | No |
| 15 | `PATH_11` | `tile_floor_11.png` | No |
| 16 | `PATH_12` | `tile_floor_12.png` | No |
| 17 | `WALL_CAP` | `tile_wall_cap.png` | Yes |
| 18 | `WALL_CPU` | `tile_wall_cpu.png` | Yes |
| 19 | `WALL_HEATSINK` | `tile_wall_heatsink.png` | Yes |
| 20 | `WALL_HEATSINK_HORIZONTAL` | `tile_wall_heatsink(horizotal).png` | Yes |
| 21 | `WALL_SLOT` | `tile_wall_slot.png` | Yes |
| 22 | `WALL_VERTICAL` | `tile_wall(vertical).png` | Yes |

`*` `EXIT` has special intended behaviour through `blocksMovement(boolean hasKey)`: it blocks movement when `hasKey` is false. However, the current `CollisionSystem` checks `isWall()` rather than calling `blocksMovement(...)`, so this key-gated exit behaviour is not currently wired into Player movement.

### Tile ID 5 migration warning

The old `TileType.ENEMY(5, ...)` entry was removed so enemies could become separate objects. **Do not reintroduce `ENEMY` as a tile merely to make old maps load.**

The current `level1.properties` still has tile code `5` at `(10,7)` and `(13,9)`. Since `TileType.fromCode(5)` now throws `IllegalArgumentException`, loading that map with the current enum will fail. If Level 1 is to be playable, migrate those old enemy cells to the new `enemy=...` metadata format and replace their terrain tile code with an appropriate walkable tile ID. Verify the exact coordinates and level design before editing. The current startup selects Level 3, so this legacy issue does not block that startup path.

---

## 7. Class-by-class technical reference

### 7.1 `MazeRunnerGame`

**Path:** `core/src/main/java/com/azset/mazerunner/MazeRunnerGame.java`

Extends libGDX `Game` and owns the top-level runtime objects:

- `SpriteBatch`, player texture, camera, viewport;
- `GameMap`, `MapRenderer`, `Player`;
- `CollisionSystem`;
- `EnemyManager`, `EnemyRenderer`;
- `BitmapFont` and a currently unused/underused `ShapeRenderer` field.

It chooses the current level, creates entities, controls the frame order, follows the player with the camera, draws the map/enemies/player, and disposes owned rendering resources. Keep this class as the coordinator; avoid moving domain algorithms into it.

### 7.2 `GameObject`

**Path:** `core/src/main/java/com/azset/mazerunner/model/GameObject.java`

Abstract base class for positioned/sized world entities. Stores protected `x`, `y`, `width`, and `height`; constructor initializes them; `update(float delta)` is abstract; getter methods expose position/size; `setPosition()` updates the base position.

`Player` and `Enemy` extend it. Keep shared spatial state here unless a concrete design need justifies a more complex hierarchy.

### 7.3 `Player`

**Path:** `core/src/main/java/com/azset/mazerunner/model/Player.java`

Current known behaviour:

- Extends `GameObject`.
- Base sprite size is supplied by `MazeRunnerGame` (currently 12×12).
- Movement speed is `200f` world units/second.
- Starts with 3 health points.
- Owns a `BoxCollider` hitbox of 10×10, centered inside the sprite.
- `move(dx, dy, dt)` refuses movement after death, updates world position, then updates hitbox position.
- `setPosition()` also updates the hitbox; this matters because `CollisionSystem` rolls the player back after blocked movement.
- `update(delta)` decreases the damage cooldown to zero.
- `takeDamage(damage)` ignores invalid damage, dead state, and active cooldown; subtracts health; starts a 2-second cooldown; prints health and `GAME OVER` to standard output when health reaches zero.
- Exposes `getHealth()`, `isDead()`, and `isInvulnerable()`.

Any new way of changing Player position must also keep the hitbox synchronized. Avoid writing directly to Player's position fields outside the class.

### 7.4 `BoxCollider`

**Path:** `core/src/main/java/com/azset/mazerunner/collision/BoxCollider.java`

Small axis-aligned bounding-box utility with position and size. `overlaps(BoxCollider other)` checks rectangle intersection using strict overlap comparisons. `setPosition()` moves the existing collider. It does not own or render a game object and does not perform map lookup.

Use it as the common low-level overlap primitive rather than duplicating the four AABB comparisons in feature classes.

### 7.5 `CollisionSystem`

**Path:** `core/src/main/java/com/azset/mazerunner/collision/CollisionSystem.java`

Owns Player-versus-map collision. It reads the Player hitbox, computes the tile cells covered by that hitbox, treats out-of-bounds space as blocked, converts world Y to map Y, and checks tile wall flags. `movePlayer()` applies X and Y movement separately and rolls back a blocked axis. This axis-separated movement enables sliding along walls.

**Current limitation:** It currently tests `map.getTile(x, mapY).isWall()`. It does not currently pass Player key state into `TileType.blocksMovement(hasKey)`, and it does not treat `TRAP` as a damage collision. Do not claim locked-exit or trap gameplay is finished.

### 7.6 `MapLoader`

**Path:** `core/src/main/java/com/azset/mazerunner/map/MapLoader.java`

Loads a properties file using `Gdx.files.internal(path)`, calls `Properties.load(InputStream)`, closes the stream with try-with-resources, and returns `new GameMap(properties)`. I/O errors are wrapped in a `RuntimeException` identifying the map path.

Use this existing loader for runtime map loading; do not make game classes parse `.properties` themselves.

### 7.7 `GameMap`

**Path:** `core/src/main/java/com/azset/mazerunner/map/GameMap.java`

Responsibilities:

- Stores `width`, `height`, `tileSize`, and `TileType[][]` terrain data.
- Parses terrain properties into the tile grid.
- Tracks whether every terrain coordinate was assigned.
- Enforces positive dimensions, coordinate bounds, unique terrain coordinates, exactly one `ENTRY`, exactly one `EXIT`, boundary placement for `ENTRY`, and complete tile coverage.
- Separately parses key, life-pickup, and enemy object coordinate metadata.
- Provides map dimension accessors, `getTile(x,y)`, entry lookup, and `getEnemyCoordinates()`.

Important details:

- Terrain is stored as `tiles[y][x]`.
- `getTile()` bounds-checks and throws if the requested coordinate is outside the map.
- `addObjectCoordinate()` requires exactly two comma-separated values and checks map bounds.
- The current `getEnemyCoordinates()` returns the internal list directly. Do not mutate it from unrelated systems.
- Current object metadata validation is weaker than terrain validation: object positions are not checked against walls, duplicates, or other objects.

### 7.8 `TileType`

**Path:** `core/src/main/java/com/azset/mazerunner/map/TileType.java`

Enum binding numeric terrain IDs to filenames and a boolean wall property. `fromCode()` searches the enum and throws for unknown numeric IDs. `isWall()` exposes the wall flag. `blocksMovement(hasKey)` blocks all walls and blocks `EXIT` when the player has no key.

Changing the enum requires auditing every map file because numeric IDs are persisted in map data.

### 7.9 `MapRenderer`

**Path:** `core/src/main/java/com/azset/mazerunner/map/MapRenderer.java`

Uses an `EnumMap<TileType, Texture>` to load one texture for every tile enum value. Runtime asset base path is `maze_runner_sprites/assets/`; it appends each enum's `getAssetName()`. It loops over the map grid, converts map Y into world Y, and draws each tile with the shared SpriteBatch. `dispose()` disposes all textures.

The renderer draws terrain only. Enemies are not terrain and must be drawn by `EnemyRenderer`.

### 7.10 `EnemyState`

**Path:** `core/src/main/java/com/azset/mazerunner/model/EnemyState.java`

Two values: `PATROL` and `CHASE`.

### 7.11 `Enemy`

**Path:** `core/src/main/java/com/azset/mazerunner/model/Enemy.java`

- Extends `GameObject`.
- Default speed is `60f`.
- Starts in `PATROL`.
- Speed is multiplied by `1.4f` while in `CHASE`.
- Owns a persistent `BoxCollider` hitbox sized to 70% of its sprite size and centered within the sprite.
- Both `move()` and `setPosition()` update the hitbox position.
- `update(float delta)` is currently empty; movement/state decisions are owned by `EnemyManager`.

Do not update Enemy position without synchronizing its hitbox. `EnemyManager` uses both `move()` and `setPosition()`.

### 7.12 `EnemyAI`

**Path:** `core/src/main/java/com/azset/mazerunner/model/EnemyAI.java`

Provides grid pathfinding helpers for EnemyManager:

- Uses four cardinal neighbours (right, left, up, down).
- Treats out-of-bounds cells as not walkable.
- Uses `!map.getTile(x, y).blocksMovement(false)` as the walkability rule.
- Uses Breadth-First Search (BFS) with a queue, visited array, and parent array.
- Finds a shortest grid path in an unweighted map and returns the first tile step after the current tile, or `null` if no valid route is available.

It operates in map-grid coordinates, not world-space pixels. Be careful to convert Enemy and Player world positions into map tile coordinates before calling it.

### 7.13 `EnemyManager`

**Path:** `core/src/main/java/com/azset/mazerunner/model/EnemyManager.java`

Owns the Enemy collection, target positions, EnemyAI, random patrol choice, and update logic.

Current behaviour:

- Constructor receives `GameMap`, creates `EnemyAI`, and spawns enemies.
- Spawning reads `map.getEnemyCoordinates()`; it no longer searches for `TileType.ENEMY`.
- Each Enemy is 75% of a tile size and is centered in the designated tile.
- Each Enemy has a corresponding target position stored in a parallel `List<float[]>`.
- Each update recalculates whether the Enemy should chase the player, moves toward its current target, and chooses another target when it reaches one.
- Detection distance is 5 tiles; it returns to Patrol after the player is farther than 7 tiles.
- In Chase, it asks `EnemyAI.findNextStep()` for the next BFS step toward the player's tile.
- If not chasing or no chase step is available, it chooses a random walkable cardinal neighbour for patrol.
- `getEnemies()` exposes the Enemy list for rendering/collision.

The coordinate conversion inside `chooseNextTarget()` is important. Do not mix map row coordinates with world-space Y. Current pathfinding is grid-based; do not assume it includes avoidance of other enemies or dynamic obstacles.

### 7.14 `EnemyCollision`

**Path:** `core/src/main/java/com/azset/mazerunner/model/EnemyCollision.java`

Uses `player.getHitbox()` and `enemy.getHitbox()`, then calls the shared `BoxCollider.overlaps()`. When a collision is detected, it calls `player.takeDamage(1)` and breaks out of the loop. Player's own two-second cooldown prevents repeated health reduction while contact continues.

This class should coordinate entity collision, not create a second collision primitive.

### 7.15 `EnemyRenderer`

**Path:** `core/src/main/java/com/azset/mazerunner/model/EnemyRenderer.java`

Loads one Enemy texture from `maze_runner_sprites/assets/enemy.png`, sets nearest-neighbour filtering, and draws every Enemy with the shared SpriteBatch at its current position/size. It disposes its texture in `dispose()`.

The file exists in the active asset directory at:

```text
assets/maze_runner_sprites/assets/enemy.png
```

### 7.16 `Lwjgl3Launcher`

**Path:** `lwjgl3/src/main/java/com/azset/mazerunner/lwjgl3/Lwjgl3Launcher.java`

Desktop-only bootstrap. It constructs the LWJGL3 application, creates the `MazeRunnerGame`, and sets window title, VSync, foreground FPS, dimensions, and resizability. Avoid putting gameplay logic here.

---

## 8. Asset conventions

### Runtime asset root

The current desktop module uses `assets/` as its resource root. Game code should refer to paths relative to that root.

Examples:

```java
Gdx.files.internal("maze_runner_sprites/assets/player1.png");
Gdx.files.internal("maze_runner_sprites/assets/enemy.png");
Gdx.files.internal("maps/level3.properties");
```

### Current active texture set

Runtime code uses:

```text
assets/maze_runner_sprites/assets/
```

Alternative folders `assets_pixel/` and `assets_pixel_v2/` are present in the repository, but the shown runtime code does not select them. Do not assume changing an image in an alternate folder changes the running game.

### Filenames are exact

TileType filenames must match actual file names exactly. Current names include spelling/parentheses that look unusual, such as:

- `tile_wall_heatsink(horizotal).png` — the current enum and filename use `horizotal` (not `horizontal`).
- `tile_wall(vertical).png`

Do not "correct" a spelling in only one place. If a filename is deliberately corrected, update the enum and file together and verify all maps/build output.

Use Nearest filtering for the current pixel-art sprites unless the art direction intentionally changes.

---

## 9. Build, run, and verification commands

Run commands from the repository root.

### Linux/macOS

```bash
# Compile core Java source
./gradlew :core:compileJava

# Launch the desktop game
./gradlew lwjgl3:run

# Run configured tests
./gradlew test

# Clean generated build output
./gradlew clean

# Check whitespace/conflict markers in tracked changes
git diff --check
```

### Windows PowerShell

```powershell
# Compile core Java source
.\gradlew.bat :core:compileJava

# Launch the desktop game
.\gradlew.bat lwjgl3:run

# Run tests
.\gradlew.bat test
```

### Expected runtime verification for gameplay changes

For a Player/map change:

- Player starts at the `ENTRY` tile.
- Player cannot walk through walls or leave map bounds.
- Movement remains responsive in both axes and can slide along walls.
- Player texture is visible and camera remains within map bounds.

For Enemy changes:

- Every `enemy` metadata entry creates exactly one visible Enemy.
- Enemy appears on the intended walkable tile, not at an inverted Y coordinate.
- Patrol movement remains inside walkable cells.
- Enemy changes to Chase within the configured detection range and returns to Patrol beyond the lose range.
- Player and Enemy hitboxes stay synchronized with their sprites.
- Contact reduces health by one, then the damage cooldown prevents immediate repeated damage.

A successful `:core:compileJava` does not prove that textures load or the runtime behaves correctly. Launch and test the game as well.

---

## 10. Git and team collaboration

The repository has a shared `main` branch and collaborator feature branches. The branch list observed in the team's GitHub view included `amil`, `emin`, `nuray`, `fakhriyya`, and `cavad`; branch existence and ownership can change, so confirm with Git before using them.

### Recommended workflow

```bash
# Start from an updated main branch
git switch main
git pull --ff-only origin main

# Create a task-specific branch
git switch -c <your-branch-or-feature-name>

# Before and after editing
git status --short
git diff --check
./gradlew :core:compileJava

# Stage only files related to the task
git add path/to/file1 path/to/file2

git diff --cached --stat
git diff --cached --check

# Commit only when asked/approved
git commit -m "Describe the change"
```

Merge or pull-request strategy should follow the team's current decision. Do not use `git merge -X ours`, `git reset --hard`, `git clean -fd`, `git push --force`, or mass restore commands casually. These can discard teammates' work or hide conflicts.

Before switching branches, inspect `git status`. If there are uncommitted changes, understand and preserve them rather than overwriting them.

---

## 11. Implemented versus incomplete features

This section deliberately separates what the current source demonstrates from what is only suggested by names, assets, or map metadata.

| Feature/system | Current status based on source |
|---|---|
| Desktop LWJGL3 startup | Implemented |
| Fixed selected map loading through MapLoader | Implemented |
| Tile grid parsing and validation | Implemented |
| Unique tile texture mapping | Implemented |
| Camera follow, bounds clamp, mouse-wheel zoom | Implemented |
| Player movement with wall collision | Implemented |
| Player health and damage cooldown | Implemented |
| Enemy object metadata parsing | Implemented |
| Enemy spawn from metadata | Implemented |
| Enemy patrol/chase state | Implemented |
| BFS enemy pathfinding | Implemented |
| Enemy rendering | Implemented |
| Player-versus-enemy collision | Implemented |
| Key metadata parsing | Partial; no full pickup/render/gameplay flow confirmed |
| Life-pickup metadata parsing | Partial; no full pickup/render/gameplay flow confirmed |
| Key collection and exit unlocking | Not wired into current Player/CollisionSystem frame flow |
| Trap damage/activation | Tile exists and is rendered; hazard gameplay not confirmed in current collision code |
| HUD displaying health/key state | Font is created but current shown `render()` does not draw a HUD |
| Main menu, pause screen, level selection UI | Not present in the current code paths inspected |
| Audio/music | Not present in the current code paths inspected |
| Automated gameplay tests | Not verified; JUnit dependencies are configured |

Do not upgrade a Partial/Not wired item to Implemented in documentation unless the source code and runtime behaviour have actually been added and tested.

---

## 12. Known issues and risks to check before extending the game

These are concrete observations from the current source snapshot, not instructions to immediately refactor everything.

### 12.1 Level 1 contains obsolete tile ID 5

`TileType` no longer defines ID `5` because Enemy moved to object metadata. The current `level1.properties` still contains old `=5` terrain entries at `(10,7)` and `(13,9)`. Loading Level 1 will fail at `TileType.fromCode(5)` unless those entries are migrated.

**Safe migration direction:** change those old Enemy cells to a walkable terrain tile and add corresponding `enemy` metadata entries, after confirming the intended level design. Do not re-add a terrain Enemy enum just to bypass the map migration.

### 12.2 Exit key gating is not connected to movement collision

`TileType.blocksMovement(boolean hasKey)` contains a key-aware Exit rule. `EnemyAI` uses `blocksMovement(false)`. But current `CollisionSystem` checks only `isWall()`, so it does not enforce the intended locked-exit rule for the Player. A future key feature must connect Player key state to collision checks deliberately.

### 12.3 Key/life pickup systems are incomplete

`GameMap` parses metadata lists for key and life pickup positions, but current source does not show public getters for both lists or dedicated object managers/renderers/collection handling. Do not add a second parser. Extend the existing metadata-to-object flow in small steps.

### 12.4 Trap behaviour is not shown in CollisionSystem

`TRAP` has a texture and a tile ID, but its enum wall flag is false and current Player collision only checks walls. A trap may render without currently causing damage. If implementing traps, add hazard handling without making traps impassable unless that is an explicit design choice.

### 12.5 Object positions are only bounds-validated

`GameMap.addObjectCoordinate()` verifies that metadata is inside the map. It does not verify that the tile is walkable. Map authors must currently put Enemy/object metadata over appropriate walkable cells. A future validation feature could check object/tile compatibility, but should preserve legitimate overlap rules (for example, a key can sit on a floor tile).

### 12.6 Map metadata and terrain are parsed from Java Properties

Properties key iteration order is not a reliable gameplay ordering contract. If a future system depends on stable ordering, explicitly sort/index the metadata rather than relying on `stringPropertyNames()` iteration order.

### 12.7 Gradle Java home path is machine-specific

See Section 2. Coordinate a portable solution before committing machine-local paths.

### 12.8 Rendering/input cleanup should be isolated

The current `MazeRunnerGame` has fields/imports that appear unused or underused, and it installs its scroll `InputAdapter` during rendering. These can be cleaned up later, but do not mix unrelated cleanup into a feature PR unless needed for correctness.

---

## 13. Extension patterns the team should follow

### 13.1 Adding a new terrain tile

1. Choose a numeric ID that is not already used and is compatible with existing map files.
2. Add the enum entry with the exact runtime asset filename and correct wall flag.
3. Add the texture to `assets/maze_runner_sprites/assets/`.
4. Update any map files that need it.
5. Test `TileType.fromCode()`, map loading, rendering, and collision.
6. Never reuse ID 5 for Enemy; Enemy is an object, not a terrain type.

### 13.2 Adding a new placeable object

Follow the existing split:

1. Add a clearly namespaced object property such as `newObject=column,row` and numbered entries as `newObject.2=...`.
2. Parse it in `GameMap` using the existing coordinate parser pattern.
3. Expose a deliberate accessor for the object-coordinate collection.
4. Create the runtime object/manager from the `GameMap` metadata.
5. Render it through its renderer, not `MapRenderer` if it is an entity.
6. Add collision/interaction through existing `BoxCollider` and/or collision orchestration.
7. Verify top-left map row to bottom-left world Y conversion.
8. Validate the object is on a suitable terrain tile.

Avoid encoding moving entities as tile IDs. Tiles represent the map's static terrain; entities have world positions and behaviour.

### 13.3 Adding a new Enemy behaviour

Keep responsibilities separated:

- `EnemyState` — state labels.
- `Enemy` — entity state, movement, dimensions, hitbox, speed.
- `EnemyAI` — grid/path decisions.
- `EnemyManager` — spawning, updating, target selection, enemy collection.
- `EnemyCollision` — Player/Enemy overlap and damage response.
- `EnemyRenderer` — texture loading and drawing.
- `MazeRunnerGame` — creates and calls these systems in the frame lifecycle.

Do not copy BFS code into `EnemyManager`, put rendering code into `Enemy`, or put enemy path logic into `MazeRunnerGame`.

### 13.4 Adding a player-facing feature

Keep `Player` as the owner of Player state (health, speed, future inventory/key state). Keep map collision in `CollisionSystem`, entity overlaps in the relevant collision coordinator, and drawing in renderer/game rendering code. Make sure any Player position change updates its hitbox.

---

## 14. Suggested verification checklist for pull requests

Before a feature is considered integrated:

- [ ] Read the current `main` and feature branch history.
- [ ] Confirm working tree changes belong to the task.
- [ ] Preserve existing package responsibilities.
- [ ] Preserve all existing tile IDs unless a migration is explicitly included.
- [ ] Check map coordinate orientation and world-space conversion.
- [ ] Check runtime resource paths from the assets root.
- [ ] Ensure every created disposable resource is disposed.
- [ ] Keep hitboxes synchronized with the corresponding objects.
- [ ] Run `./gradlew :core:compileJava`.
- [ ] Run `./gradlew test` where relevant.
- [ ] Run `./gradlew lwjgl3:run` for rendering/gameplay changes.
- [ ] Manually test the changed feature and nearby existing mechanics.
- [ ] Run `git diff --check`.
- [ ] Review `git diff` and stage only relevant files.
- [ ] Update this document if architecture, commands, file paths, tile IDs, or feature status change.

---

## 15. Prompt template for a teammate using an AI assistant

The following prompt can be copied together with this file:

> You are helping with the Java/libGDX Maze Runner project. Read `AllInfosForAI.md` completely before making recommendations. Then inspect the current source files and Git state because the document may be behind the code. Preserve the existing architecture and working core systems. Do not rewrite or refactor unrelated code. Make the smallest necessary incremental change and explain exactly which classes are involved and how the new code integrates with existing systems. Do not invent implemented features. Keep tile IDs stable, respect the map/world Y-axis conversion, reuse `BoxCollider`/`CollisionSystem`, and use the existing assets resource root. Before declaring success, compile and run the relevant manual checks. Do not commit or push unless I explicitly ask.

---

## 16. Source-of-truth hierarchy

The repository root also contains `plan.docx`. It is a project artefact and may contain assignment goals, requirements, or planning information that is not represented in runtime Java code. Its contents were not independently extracted for this context file, so an AI must not invent what it says. When working on requirements, open and read `plan.docx` as well, then distinguish assignment requirements from features that are actually implemented.

When information conflicts, use this order:

1. **Current checked-out source code and current map/assets files.** These define what the game actually does.
2. **Current Git history/branch state.** This defines what is committed and where.
3. **This `AllInfosForAI.md` file.** It explains architecture and conventions but may become stale.
4. Old chat transcripts, screenshots, or assumptions. These provide history, not guaranteed current truth.

For changes to project architecture or gameplay, update this document in the same feature branch so the next teammate and AI assistant are not given outdated instructions.

---

## 17. Repository references

- Repository: https://github.com/KechiZona/Meze_runner
- Main game class: `core/src/main/java/com/azset/mazerunner/MazeRunnerGame.java`
- Map parser/model: `core/src/main/java/com/azset/mazerunner/map/GameMap.java`
- Map loader: `core/src/main/java/com/azset/mazerunner/map/MapLoader.java`
- Tile definitions: `core/src/main/java/com/azset/mazerunner/map/TileType.java`
- Player model: `core/src/main/java/com/azset/mazerunner/model/Player.java`
- Enemy system: `core/src/main/java/com/azset/mazerunner/model/Enemy*.java`
- Collision system: `core/src/main/java/com/azset/mazerunner/collision/`
- Current selected map: `assets/maps/level3.properties`
- Runtime textures: `assets/maze_runner_sprites/assets/`

**End of project context.**
