package ru.drifles.crpg.object.world.navigation;

import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.renderer.NavPathRenderer;
import ru.drifles.crpg.renderer.Renderer;

public class NavPath implements Drawable {
    private final NavNode to;
    private final NavNode from;
    private final Renderer renderer;

    public NavPath(NavNode from, NavNode to) {
        this.to = to;
        this.from = from;
        this.renderer = new NavPathRenderer(this);
    }

    @Override
    public void draw() {
        renderer.render();
    }

    public NavNode getFrom() {
        return from;
    }

    public NavNode getTo() {
        return to;
    }
}
