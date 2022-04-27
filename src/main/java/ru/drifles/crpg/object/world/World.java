package ru.drifles.crpg.object.world;

import ru.drifles.crpg.object.Drawable;

import java.util.ArrayList;
import java.util.List;
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

    public Tile[][] getTiles() {
        return tiles;
    }

    public List<Tile> getNeighbors(Tile tile) {
        var result = new ArrayList<Tile>();

        var i = tile.getPosition().getY();
        var j = tile.getPosition().getX();

        if (j + 1 < worldSize)
            result.add(tiles[i][j + 1]);

        if (j - 1 >= 0)
            result.add(tiles[i][j - 1]);

        if (i + 1 < worldSize)
            result.add(tiles[i + 1][j]);

        if (i - 1 >= 0)
            result.add(tiles[i - 1][j]);

        return result;
    }
}
