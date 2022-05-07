package ru.drifles.crpg.gameobject.tile;

import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Tile implements Drawable {
    private final Position position;
    private final boolean passable;
    private final List<Way> ways;
    private final Renderer renderer;

    private Way sourceWay;

    public Tile(Position position, boolean passable) {
        this.position = position;
        this.passable = passable;
        this.ways = new ArrayList<>();
        this.renderer = new TileRenderer(this);
    }

    public void addWay(Tile to) {
        ways.add(new Way(this, to));
    }

    @Override
    public void draw() {
        renderer.render();
    }

    public Position getPosition() {
        return position;
    }

    public boolean isPassable() {
        return passable;
    }

    public List<Way> getWays() {
        return ways;
    }

    public Way getSourceWay() {
        return sourceWay;
    }

    public void setSourceWay(Way sourceWay) {
        this.sourceWay = sourceWay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return passable == tile.passable && position.equals(tile.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, passable);
    }
}
