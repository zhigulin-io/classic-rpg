package ru.drifles.crpg.object.world;

import ru.drifles.crpg.common.Position;
import ru.drifles.crpg.object.Drawable;
import ru.drifles.crpg.renderer.Renderer;
import ru.drifles.crpg.renderer.WalkerRenderer;

public class Walker implements Drawable {
    private final Position position;
    private final Renderer renderer;

    private Position target;

    public Walker(Position position) {
        this.position = position;
        this.renderer = new WalkerRenderer(this);
    }

    public Position getPosition() {
        return position;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position target) {
        this.target = target;
    }

    @Override
    public void draw() {
        renderer.render();
    }
}
