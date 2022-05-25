package ru.drifles.crpg.gameobject;

import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.gameobject.walker.Walker;

import java.io.IOException;
import java.net.URISyntaxException;

public class World {
    private final Land land;
    private final Walker walker;

    public World() throws URISyntaxException, IOException {
        this.land = new Land("/level1.world");
        this.walker = new Walker(land, new Position(1, 1));
    }

    public Land getLand() {
        return land;
    }

    public Walker getWalker() {
        return walker;
    }
}
