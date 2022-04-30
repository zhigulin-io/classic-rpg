package ru.drifles.crpg.object.world.navigation;

import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.object.world.Tile;
import ru.drifles.crpg.renderer.NavNodeRenderer;
import ru.drifles.crpg.renderer.Renderer;

import java.util.Objects;

public class NavNode implements Drawable {
    private final Tile tile;
    private final Renderer renderer;

    public NavNode(Tile tile) {
        this.tile = tile;
        this.renderer = new NavNodeRenderer(this);
    }

    @Override
    public void draw() {
        renderer.render();
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavNode navNode = (NavNode) o;
        return tile.equals(navNode.tile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tile);
    }
}
