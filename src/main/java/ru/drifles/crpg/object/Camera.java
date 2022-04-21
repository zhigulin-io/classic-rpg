package ru.drifles.crpg.object;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.Position;

public class Camera {

    private final Matrix4f matrix;

    private static Camera instance = null;

    private Camera() {
        Position position = new Position();
        int screenWidth = 160;
        int screenHeight = 160;
        this.matrix = new Matrix4f().ortho(
                position.getX(),
                position.getX() + screenWidth,
                position.getY(),
                position.getY() + screenHeight,
                -1.0f, 1.0f
        );
    }

    public static Camera getInstance() {
        if (instance == null)
            instance = new Camera();

        return instance;
    }

    public Matrix4f getMatrix() {
        return matrix;
    }
}
