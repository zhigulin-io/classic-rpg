package ru.drifles.crpg.object.world;

import ru.drifles.crpg.object.Drawable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class World implements Drawable {

    private final int worldSize = 10;
    private final Tile[][] tiles = new Tile[worldSize][worldSize];

    public World(String worldName) {

        var worldURL = getClass().getResource(worldName);

        try (var file = new BufferedReader(new FileReader(new File(Objects.requireNonNull(worldURL).toURI())))) {
            for (int i = 0; i < worldSize; i++) {
                var line = file.readLine();
                for (int j = 0; j < worldSize; j++) {
                    if (line.charAt(j) == '#')
                        tiles[i][j] = new Tile(j, i, false);
                    else
                        tiles[i][j] = new Tile(j, i, true);
                }
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
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
