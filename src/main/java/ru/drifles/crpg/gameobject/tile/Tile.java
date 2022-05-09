package ru.drifles.crpg.gameobject.tile;

import ru.drifles.crpg.common.Drawable;
import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.common.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Tile implements Drawable, Comparable<Tile> {
    private final Position position;
    private final boolean passable;
    private final List<Way> ways;
    private final Renderer renderer;

    private Way sourceWay;
    private double h;
    private double g;
    private double f;

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

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setF(double f) {
        this.f = f;
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

    @Override
    public int compareTo(Tile o) {
        return (int) (this.f - o.f);
    }
}
