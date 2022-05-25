package ru.drifles.crpg.common;

import org.joml.Matrix4f;

public final class Camera {
    private static final int TILES_NUMBER = 16;
    private static final float CAMERA_OFFSET = -0.5f;
    private static final float LEFT_POSITION = CAMERA_OFFSET;
    private static final float TOP_POSITION = CAMERA_OFFSET;
    private static final float RIGHT_POSITION = TILES_NUMBER + CAMERA_OFFSET;
    private static final float BOTTOM_POSITION = TILES_NUMBER + CAMERA_OFFSET;
    private static final float NEAR_POSITION = -1;
    private static final float FAR_POSITION = 1;

    public static final float WIDTH = RIGHT_POSITION - LEFT_POSITION;
    public static final float HEIGHT = BOTTOM_POSITION - TOP_POSITION;

    public static final Matrix4f PROJECTION = new Matrix4f().ortho(
            LEFT_POSITION,
            RIGHT_POSITION,
            BOTTOM_POSITION,
            TOP_POSITION,
            NEAR_POSITION,
            FAR_POSITION
    );

    private Camera() {  }
}
