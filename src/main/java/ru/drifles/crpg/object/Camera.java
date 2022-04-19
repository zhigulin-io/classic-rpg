package ru.drifles.crpg.object;

import org.joml.Matrix4f;
import ru.drifles.crpg.common.Position;

public class Camera {

    private final Matrix4f matrix;

    private final int screenWidth = 200;
    private final int screenHeight = 200;

    private static Camera instance = null;

    private final Position position;

    private Camera() {
        this.position = new Position();
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

    public void moveUp() {
        position.moveUp();
        updateMatrix();
    }

    public void moveDown() {
        position.moveDown();
        updateMatrix();
    }

    public void moveLeft() {
        position.moveLeft();
        updateMatrix();
    }

    public void moveRight() {
        position.moveRight();
        updateMatrix();
    }

    private void updateMatrix() {
        float left = screenWidth * position.getX();
        float bottom = screenHeight * position.getY();
        matrix.setOrtho(left, left + screenWidth, bottom, bottom + screenHeight, -1.0f, 1.0f);
    }
}
