package ru.drifles.crpg.object;

import ru.drifles.crpg.common.Position;

public abstract class GameObject implements Drawable {
    protected Position position;

    public Position getPosition() {
        return position;
    }
}
