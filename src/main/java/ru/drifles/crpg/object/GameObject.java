package ru.drifles.crpg.object;

import ru.drifles.crpg.common.Position;

public abstract class GameObject implements Drawable {
    protected final Position position;

    public GameObject() {
        this.position = new Position();
    }

    public GameObject(int x, int y) {
        this.position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }
}
