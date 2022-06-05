package ru.drifles.crpg.model;

import java.io.IOException;
import java.net.URISyntaxException;

public class World {
    private final Land land;
    private final Walker walker;

    public World() throws URISyntaxException, IOException {
        this.land = new Land("/level_small_3.world");
        this.walker = new Walker(land, new Position(1, 1));
    }

    public Land getLand() {
        return land;
    }

    public Walker getWalker() {
        return walker;
    }
}
