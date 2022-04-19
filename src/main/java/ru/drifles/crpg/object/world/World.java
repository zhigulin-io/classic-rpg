package ru.drifles.crpg.object.world;

import ru.drifles.crpg.object.Drawable;

import java.util.ArrayList;
import java.util.List;

public class World implements Drawable {

    List<Tile> tiles = new ArrayList<>();

    public World() {
        for (int i = 1; i < 12; i++) {
            for (int j = 1; j < 12; j++) {
                tiles.add(new Tile(j, i));
            }
        }
    }

    @Override
    public void draw() {
        tiles.forEach(Tile::draw);
    }
}
