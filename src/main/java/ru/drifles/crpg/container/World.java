package ru.drifles.crpg.container;

import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.gameobject.walker.Walker;

public class World implements Drawable {
    private final Land land = new Land("/level1.world");
    private final Walker walker = new Walker(new Position(1, 1));

    @Override
    public void draw() {
        land.draw();
        walker.draw();
    }

    public Land getLand() {
        return land;
    }

    public Walker getWalker() {
        return walker;
    }
}
