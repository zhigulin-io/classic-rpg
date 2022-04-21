package ru.drifles.crpg.object.world;

import ru.drifles.crpg.object.Drawable;

import java.util.Random;

public class World implements Drawable {

    private final int worldSize = 10;
    private final Tile[][] tiles = new Tile[worldSize][worldSize];

    public World() {
        var rnd = new Random();
        for (int i = 0; i < worldSize; i++) {
            for (int j = 0; j < worldSize; j++) {
                var passable = rnd.nextBoolean();
                tiles[i][j] = new Tile(j, i, passable);
            }
        }
    }

    @Override
    public void draw() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                tile.draw();
    }
}
