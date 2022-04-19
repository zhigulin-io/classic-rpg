package ru.drifles.crpg.object.world;

import ru.drifles.crpg.object.Drawable;

public class World implements Drawable {

    private final int worldSize = 11;
    private final Tile[][] tiles = new Tile[worldSize][worldSize];

    public World() {
        for (int i = 0; i < worldSize; i++) {
            for (int j = 0; j < worldSize; j++) {
                tiles[i][j] = new Tile(j + 1, i + 1);
            }
        }
    }

    @Override
    public void draw() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                tile.draw();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public int getWorldSize() {
        return worldSize;
    }
}
